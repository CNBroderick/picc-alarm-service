package ape.alarm.common.wos.scanner;

import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.wos.AlarmWosSender;
import ape.alarm.common.wos.core.AlarmWosStatus;
import ape.alarm.entity.po.AlarmWos;
import ape.alarm.service.po.AlarmWosInfoService;
import ape.alarm.service.po.AlarmWosService;
import ape.master.common.parameter.ParameterEnum;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class AlarmWosSendScanner {

    public static final String ALARM_WOS_SEND_QUEUE_REDIS_KEY = AlarmWosSender.ALARM_WOS_SEND_QUEUE_REDIS_KEY;
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警工单发送后台服务, ParameterEnum.后台服务.告警工单发送后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;
    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmWosService alarmWosService;
    private final AlarmWosInfoService alarmWosInfoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private final URI uri;

    public AlarmWosSendScanner(StringRedisTemplate stringRedisTemplate,
                               AlarmWosService alarmWosService,
                               AlarmWosInfoService alarmWosInfoService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.alarmWosService = alarmWosService;
        this.alarmWosInfoService = alarmWosInfoService;

        try {
            uri = new URI(ParameterEnum.后台接口.告警工单系统服务器地址.getEntryValue("http://10.8.25.252:8080/createWF4IM"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0/1 * * ? * *")
    public void execute() {
        if (IS_RUNNING) {
            log.info("APE告警工单发送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            List<String> postDataList = stringRedisTemplate.opsForSet().pop(ALARM_WOS_SEND_QUEUE_REDIS_KEY, 16);
            while (postDataList != null && !postDataList.isEmpty()) {
                for (String s : postDataList) {
                    Future<?> submit = executorService.submit(() -> send(s));
                }
                postDataList = stringRedisTemplate.opsForSet().pop(ALARM_WOS_SEND_QUEUE_REDIS_KEY, 16);
            }
        } catch (Exception e) {
            log.error("告警工单发送出错。", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
        }
    }

    @SneakyThrows
    private void send(String postData) {
        GsonJsonObjectUtil json = new GsonJsonObjectUtil(postData);
        try {
            log.info("\n### 请求发送告警工单[" + json.string("uuid") + "]\nPOST " + uri.toString() + "\n" + json.pretty());

            GsonJsonObjectUtil response = new GsonJsonObjectUtil(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(postData)).build(), HttpResponse.BodyHandlers.ofString()).body());

            log.info("\n告警工单响应结果[" + json.string("uuid") + "]\n" + response.pretty());

            String uuid = response.string("uuid");
            AlarmWos alarmWos = alarmWosService.selectFirstByUuid(uuid);

            alarmWos.setResMessage(response.string("resmsg")).setResCode(response.getInt("rescode"));
            if (alarmWos.getResCode() > 0) {
                alarmWosService.updateByPrimaryKey(alarmWos.setStatus(AlarmWosStatus.创建失败.name()));
                return;
            }
            alarmWos.setStatus(AlarmWosStatus.已创建.name()).setWfNum(response.string("wfnum")).setWfId(response.string("wfid"));
            alarmWosService.updateByPrimaryKey(alarmWos);

            Set<Integer> alarmIds = new LinkedHashSet<>();
            for (JsonElement element : json.jsonArray("alarminfo", new JsonArray())) {
                GsonJsonObjectUtil info = new GsonJsonObjectUtil(element);
                int mntno = info.getInt("mntno", 0);
                if (mntno > 0) alarmIds.add(mntno);
            }

            alarmWosInfoService.updateSendTimeByWosIdAndAlarmIdIn(LocalDateTime.now(), alarmWos.getId(), alarmIds);
        } catch (ConnectException e) {
            log.error("连接到告警工单服务器超时，重新将工单[" + json.string("uuid") + "]加入发送队列。", e);
            stringRedisTemplate.opsForSet().add(ALARM_WOS_SEND_QUEUE_REDIS_KEY, postData);
        } catch (Exception e) {
            log.error("发送告警工单[" + json.string("uuid") + "]时出错。", e);
            stringRedisTemplate.opsForSet().add(ALARM_WOS_SEND_QUEUE_REDIS_KEY, postData);
        }

    }
}
