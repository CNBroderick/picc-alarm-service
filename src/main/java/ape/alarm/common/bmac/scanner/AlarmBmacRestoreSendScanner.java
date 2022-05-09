package ape.alarm.common.bmac.scanner;

import ape.alarm.common.bmac.AlarmBmacSender;
import ape.alarm.common.bmac.core.AlarmBmacStatus;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.entity.po.AlarmBmac;
import ape.alarm.entity.po.AlarmBmacData;
import ape.alarm.service.po.AlarmBmacDataService;
import ape.alarm.service.po.AlarmBmacService;
import ape.master.common.parameter.ParameterEnum;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class AlarmBmacRestoreSendScanner {

    public static final String ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY = AlarmBmacSender.ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY;
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(ParameterEnum.后台服务.启用蓝鲸告警中心恢复后台服务, ParameterEnum.后台服务.告警蓝鲸告警中心恢复后台服务最后运行时间);

    public static boolean IS_RUNNING = false;
    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmBmacService alarmBmacService;
    private final AlarmBmacDataService alarmBmacDataService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private final URI uri;
    private final String xSecret;

    public AlarmBmacRestoreSendScanner(StringRedisTemplate stringRedisTemplate,
                                       AlarmBmacService alarmBmacService,
                                       AlarmBmacDataService alarmBmacDataService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.alarmBmacService = alarmBmacService;
        this.alarmBmacDataService = alarmBmacDataService;

        try {
            uri = new URI(ParameterEnum.后台接口.蓝鲸告警中心推送服务器地址.getEntryValue("http://127.0.0.1:9455/api/bmac/alarm"));
            xSecret = ParameterEnum.后台接口.蓝鲸告警中心密钥.getEntryValue("Q0bxS1vQMK1hy2YbiZdqETVx1pwNIThR");
        } catch (Exception e) {
            throw new RuntimeException("获取蓝鲸告警中心相关参数失败。", e);
        }
    }

    @Scheduled(cron = "0/1 * * ? * *")
    public void execute() {
        if (IS_RUNNING) {
            log.info("蓝鲸告警中心恢复推送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            Integer id = getNextId();
            while (id != null) {
                id = sendToBmac(id);
            }
        } catch (Exception e) {
            log.error("蓝鲸告警中心恢复告警推送出错。", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
        }
    }

    @SneakyThrows
    private Integer sendToBmac(Integer id) {
        if (id == null) return getNextId();
        AlarmBmac bmac = alarmBmacService.getById(id);
        AlarmBmacData data = alarmBmacDataService.getById(id);
        if (bmac == null || data == null) {
            return getNextId();
        }

        doSend(bmac, data);

        return getNextId();
    }

    private void doSend(AlarmBmac bmac, AlarmBmacData data) {
        try {
            GsonJsonObjectUtil json = new GsonJsonObjectUtil(data.request());
            log.info("\n### 向蓝鲸告警中心推送恢复告警[alarm_id=" + bmac.aid() + "]\nPOST " + uri.toString() + "\n" + json.pretty());

            HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(HttpRequest.newBuilder().header("X-Secret", xSecret)
                    .uri(uri).POST(HttpRequest.BodyPublishers.ofString(data.restoreRequest().toString(), StandardCharsets.UTF_8))
                    .build(), HttpResponse.BodyHandlers.ofString());

            GsonJsonObjectUtil response = new GsonJsonObjectUtil(httpResponse.body());
            log.info("\n### 蓝鲸告警中心返回恢复告警[alarm_id=%d]结果：\n%s".formatted(bmac.aid(), response.pretty()));
            int code = response.getInt("code");
            String message = response.string("message");
            AlarmBmacStatus status = AlarmBmacStatus.已关闭;
            if (code != 1200) {
                log.error("\n### 向蓝鲸告警中心推送恢复告警[alarm_id=%d]失败。\n错误%d：%s。".formatted(bmac.aid(), code, message));
                status = AlarmBmacStatus.关闭失败;
                return;
            }
            alarmBmacService.updateByPrimaryKey(bmac.status(status.name()));
            alarmBmacDataService.updateByPrimaryKey(data.restoreCode(code).restoreResponse(response.get()));
        } catch (ConnectException e) {
            log.error("连接到蓝鲸告警中心超时，重新将恢复告警[id=" + bmac.id() + "]加入发送队列。", e);
            stringRedisTemplate.opsForSet().add(ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY, bmac.id() + "");
        } catch (Exception e) {
            log.error("\n### 向蓝鲸告警中心推送恢复告警[alarm_id=%d]时发生错误，原因：%s。".formatted(bmac.aid(), e.getMessage()), e);
        }
    }

    private Integer getNextId() {
        String pop = stringRedisTemplate.opsForSet().pop(ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY);
        if (pop == null) return null;
        return Integer.parseInt(pop);
    }
}
