package ape.alarm.common.email.scanner;

import ape.alarm.common.core.AlarmSenderManager;
import ape.alarm.common.email.restored.AlarmRestoredPredicate;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.parameter.ParameterEnum;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.time.RunningTime;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@EnableScheduling
public class ApeAlarmRestoredScanner {
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警恢复后台服务, ParameterEnum.后台服务.告警恢复后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;

    @Scheduled(cron = "0 */1 * * * ?")
    public void checkRestored() {
        if (IS_RUNNING) {
            log.info("APE告警发送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            RunningTime runningTime = new RunningTime();
            Map<Integer, LocalDateTime> restoredTimeMap = findNotRestoredAlarmId(Duration.ofSeconds((long)
                    ParameterEnum.告警系统.告警恢复等待时间.getEntryValue(15d) * 60L));
            int sendRestoreAlarms = sendRestoreAlarms(restoredTimeMap);
            log.info("APE告警恢复发送服务执行完毕，线程ID = {}，恢复告警 = {}条，发送告警恢复通知 = {}条，用时 = {}。",
                    Thread.currentThread().getId(), restoredTimeMap.size(), sendRestoreAlarms, runningTime.time());
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("APE告警发送服务发生错误。", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
        }
    }

    private int sendRestoreAlarms(Map<Integer, LocalDateTime> restoredTimeMap) {
        List<AlarmSendLog> sendLogs = queryAlarmSendLogs(restoredTimeMap.keySet());
        AlarmSenderManager.getInstance().sendRestore(sendLogs);
        return sendLogs.size();
    }

    private Map<Integer, LocalDateTime> findNotRestoredAlarmId(Duration duration) {
        Map<String, Integer> map = queryNotRestoredAlarm();

        Map<String, LocalDateTime> restoredTimeMap = new AlarmRestoredPredicate(duration).apply(map.keySet());

        if (!restoredTimeMap.isEmpty()) {
            new ApeOperationBuilder().add("map", restoredTimeMap).execute(ApeAlarmOperationEnum.ApeAlarmRestoredUpdateByMap);
        }
        Map<Integer, LocalDateTime> modified = new LinkedHashMap<>();
        restoredTimeMap.forEach((alarmId, datetime) -> modified.put(map.get(alarmId), datetime));
        return modified;
    }

    private Map<String, Integer> queryNotRestoredAlarm() {
        return new ApeOperationBuilder().executeQuery(ApeAlarmOperationEnum.ApeAlarmNotRestoredAlarmIdQuery, Collections::emptyMap);
    }


    private List<AlarmSendLog> queryAlarmSendLogs(Collection<Integer> aids) {
        return new ApeOperationBuilder().add("aids", aids).executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery);
    }

}
