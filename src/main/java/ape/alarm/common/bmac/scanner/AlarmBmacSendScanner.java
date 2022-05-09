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
public class AlarmBmacSendScanner {

    public static final String ALARM_BMAC_SEND_QUEUE_REDIS_KEY = AlarmBmacSender.ALARM_BMAC_SEND_QUEUE_REDIS_KEY;
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(ParameterEnum.后台服务.启用蓝鲸告警中心推送后台服务, ParameterEnum.后台服务.蓝鲸告警中心推送后台服务最后运行时间);

    public static boolean IS_RUNNING = false;
    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmBmacService alarmBmacService;
    private final AlarmBmacDataService alarmBmacDataService;

    private final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private final URI uri;
    private final String xSecret;

    public AlarmBmacSendScanner(StringRedisTemplate stringRedisTemplate,
                                AlarmBmacService alarmBmacService,
                                AlarmBmacDataService alarmBmacDataService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.alarmBmacService = alarmBmacService;
        this.alarmBmacDataService = alarmBmacDataService;

        try {
            uri = new URI(ParameterEnum.后台接口.蓝鲸告警中心推送服务器地址.getEntryValue("http://127.0.0.1:9465/api/bmac/alarm"));
            xSecret = ParameterEnum.后台接口.蓝鲸告警中心密钥.getEntryValue("Q0bxS1vQMK1hy2YbiZdqETVx1pwNIThR");
        } catch (Exception e) {
            throw new RuntimeException("获取蓝鲸告警中心相关参数失败。", e);
        }
    }

    @Scheduled(cron = "0/1 * * ? * *")
    public void execute() {
        if (IS_RUNNING) {
            log.info("蓝鲸告警中心推送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) {
            log.info("蓝鲸告警中心推送服务已被禁用。");
            return;
        }
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            Integer id = getNextId();
            while (id != null) {
                Integer id1 = id;
                log.info("蓝鲸告警中心推送开始[bmac_id=" + id1 + "]");
                id = sendToBmac(id);
                log.info("蓝鲸告警中心推送结束[bmac_id=" + id1 + "]");
            }
        } catch (Exception e) {
            log.error("蓝鲸告警中心推送出错。", e);
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
            alarmBmacService.deleteByPrimaryKey(id);
            alarmBmacDataService.deleteByPrimaryKey(id);
            log.error("获取bmac_id=" + id + "失败");
            return getNextId();
        }

        doSend(bmac, data);

        return getNextId();
    }

    private void doSend(AlarmBmac bmac, AlarmBmacData data) {
        String httpResponseBody = "";
        try {
            GsonJsonObjectUtil json = new GsonJsonObjectUtil(data.request());
            log.info("\n### 向蓝鲸告警中心推送工单[alarm_id=" + bmac.aid() + "]\nPOST " + uri.toString() + "\n" + json.pretty());

            HttpResponse<String> httpResponse = HttpClient.newHttpClient().send(HttpRequest.newBuilder().header("X-Secret", xSecret)
                    .uri(uri).POST(HttpRequest.BodyPublishers.ofString(data.request().toString(), StandardCharsets.UTF_8))
                    .build(), HttpResponse.BodyHandlers.ofString());
            httpResponseBody = httpResponse.body();
            GsonJsonObjectUtil response = new GsonJsonObjectUtil(httpResponse.body());
            log.info("\n### 蓝鲸告警中心返回告警[alarm_id=%d]结果：\n%s".formatted(bmac.aid(), response.pretty()));
            int code = response.getInt("code");
            String message = response.string("message");
            AlarmBmacStatus status = AlarmBmacStatus.已创建;
            if (code != 1200) {
                log.error("\n### 向蓝鲸告警中心推送告警[alarm_id=%d]失败。\n错误%d：%s。".formatted(bmac.aid(), code, message));
                status = AlarmBmacStatus.创建失败;
                return;
            }
            alarmBmacService.updateByPrimaryKey(bmac.code(code).message(message).status(status.name()));
            alarmBmacDataService.updateByPrimaryKey(data.code(code).response(response.get()));
        } catch (com.google.gson.JsonSyntaxException e) {
            log.error("解析蓝鲸告警中心返回结果失败[id=" + bmac.id() + "]，内容：\n" + httpResponseBody, e);
            stringRedisTemplate.opsForSet().add(ALARM_BMAC_SEND_QUEUE_REDIS_KEY, bmac.id() + "");
        } catch (ConnectException e) {
            log.error("连接到蓝鲸告警中心超时，重新将告警[id=" + bmac.id() + "]加入发送队列。", e);
            stringRedisTemplate.opsForSet().add(ALARM_BMAC_SEND_QUEUE_REDIS_KEY, bmac.id() + "");
        } catch (Exception e) {
            log.error("\n### 向蓝鲸告警中心推送告警[alarm_id=%d]时发生错误，原因：%s。".formatted(bmac.aid(), e.getMessage()), e);
        }
    }

    private Integer getNextId() {
        String pop = stringRedisTemplate.opsForSet().pop(ALARM_BMAC_SEND_QUEUE_REDIS_KEY);
        if (pop == null) return null;
        return Integer.parseInt(pop);
    }
}
