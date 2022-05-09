package ape.alarm.common.wos.scanner;

import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.common.wos.AlarmWosSender;
import ape.alarm.common.wos.builder.AlarmWosBuilder;
import ape.alarm.common.wos.core.AlarmWosStatus;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.po.AlarmWos;
import ape.alarm.entity.po.AlarmWosInfo;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.alarm.service.po.AlarmWosInfoService;
import ape.alarm.service.po.AlarmWosService;
import ape.master.common.parameter.ParameterEnum;
import ape.master.common.util.ApeAlarmIdFactory;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class AlarmWosRestoredScanner {

    public static final String ALARM_WOS_SEND_QUEUE_REDIS_KEY = AlarmWosSender.ALARM_WOS_SEND_QUEUE_REDIS_KEY;

    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警恢复工单后台服务, ParameterEnum.后台服务.告警恢复工单后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;
    private final StringRedisTemplate stringRedisTemplate;
    private final AlarmWosService alarmWosService;
    private final AlarmWosInfoService alarmWosInfoService;
    private final ExecutorService executorService = Executors.newFixedThreadPool(16);
    private final URI uri;

    public AlarmWosRestoredScanner(StringRedisTemplate stringRedisTemplate,
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

    @Scheduled(cron = "40 0/1 * ? * *")
    public void execute() {
        if (IS_RUNNING) {
            log.info("APE告警恢复工单后台服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;

            List<AlarmWos> alarmWos = alarmWosService.selectAllByCloseTimeIsNull();
            Set<String> uuids = alarmWos.stream().map(AlarmWos::getUuid).collect(Collectors.toSet());
            int duration = ParameterEnum.告警系统.告警恢复等待时间.getEntryValue(15);
            for (String uuid : uuids) {
                executorService.submit(() -> check(duration, uuid));
            }
        } catch (Exception e) {
            log.error("告警恢复工单后台服务", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
        }
    }

    private void check(int duration, String originAlarmId) {
        List<ApeAlarm> list = new ApeOperationBuilder().add("alarmId", originAlarmId).executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery);
        if (list.isEmpty()) return;
        ApeAlarm apeAlarm = list.get(0);
        List<ApeAlarm> parentAlarm = getParentAlarm(duration, apeAlarm, new LinkedHashMap<>(Map.of(apeAlarm.getId(), apeAlarm)));
        ApeAlarm lastAlarm = parentAlarm.stream().max(Comparator.comparing(ApeAlarm::getAlarmTime)).orElse(null);
        if (lastAlarm == null) return;
        if (LocalDateTime.now().minusMinutes(duration).isAfter(lastAlarm.getAlarmTime())) {
            sendRestore(originAlarmId, lastAlarm.getAlarmTime().plusMinutes(duration), lastAlarm);
        }
    }

    public void sendRestore(String originAlarmId, LocalDateTime closeTime, ApeAlarm lastAlarm) {
        try {
            AlarmWos alarmWos = alarmWosService.selectFirstByUuid(originAlarmId);
            List<AlarmWosInfo> alarmWosInfos = alarmWosInfoService.selectAllByWosId(alarmWos.getId());
            alarmWos.setIsEnd("1").setCloseTime(closeTime).setStatus(AlarmWosStatus.已关闭.name()).setMntOrder(Math.max(2, alarmWosInfos.size()));
            alarmWosInfos.forEach(info -> info.setRecoveryTime(closeTime).setFaultDuration(Duration.between(info.getPlaceTime(), closeTime).toMinutes() + ""));

            JsonObject postJson = new AlarmWosBuilder(lastAlarm).build(alarmWosInfos.stream().collect(
                    Collectors.toMap(AlarmWosInfo::getId, Function.identity())), alarmWos).createPostJson();
            log.info("\n### 请求发送告警恢复工单[" + originAlarmId + "]\nPOST " + uri.toString() + "\n" + new GsonJsonObjectUtil(postJson).pretty());

            GsonJsonObjectUtil response = new GsonJsonObjectUtil(HttpClient.newHttpClient().send(HttpRequest.newBuilder().uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(postJson.toString())).build(), HttpResponse.BodyHandlers.ofString()).body());
            log.info("\n工单响应结果[" + originAlarmId + "]\n" + response.pretty());

            if (response.getInt("rescode") > 0) {
                log.error("关闭告警恢复工单{}失败，原因：{}。", alarmWos.getWfNum(), response.string("resmsg"));
            } else {
                alarmWos.setResMessage(response.string("resmsg"));
            }
            alarmWosService.updateByPrimaryKey(alarmWos);
            alarmWosInfoService.updateBatch(alarmWosInfos);
        } catch (Exception e) {
            log.error("关闭告警工单[" + originAlarmId + "]失败。", e);
        }
    }

    private List<ApeAlarm> getParentAlarm(int duration, ApeAlarm origin, Map<Integer, ApeAlarm> stepMap) {
        if (origin == null) return sortByAlarmTime(stepMap);

        int size = stepMap.size();
        List<ApeAlarm> alarmList = new ApeOperationBuilder()
                .add("sameAlarmId", ApeAlarmIdFactory.getId(origin.getAlarmId()))
                .add("maxAlarmTime", origin.getAlarmTime().plusMinutes(duration))
                .add("minAlarmTime", origin.getAlarmTime())
                .executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery);
        alarmList.forEach(a -> stepMap.put(a.getId(), a));

        if (stepMap.size() == size) return sortByAlarmTime(stepMap);

        return getParentAlarm(duration, alarmList.stream().min(Comparator.comparing(ApeAlarm::getAlarmTime)).orElse(null), stepMap);
    }

    private List<ApeAlarm> sortByAlarmTime(Map<Integer, ApeAlarm> stepMap) {
        return stepMap.values().stream().sorted(Comparator.comparing(ApeAlarm::getAlarmTime)).collect(Collectors.toList());
    }
}
