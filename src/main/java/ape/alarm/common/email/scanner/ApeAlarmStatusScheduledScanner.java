package ape.alarm.common.email.scanner;

import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmSendStatus;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.parameter.ParameterEnum;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.time.RunningTime;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Configurable
@EnableScheduling
@Slf4j
public class ApeAlarmStatusScheduledScanner {
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警状态同步后台服务, ParameterEnum.后台服务.告警状态同步后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;

    @Scheduled(cron = "0 */10 * * * ?")
    public void execute() {
        if (IS_RUNNING) {
            log.info("APE告警发送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            RunningTime runningTime = new RunningTime();
            int processed = processErrorSendAlarms();
            log.info("APE告警发送服务发送完毕，线程ID = {}，修正状态 = {}条，用时 = {}。",
                    Thread.currentThread().getId(), processed, runningTime.time());

        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("APE告警发送服务发生错误。", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
        }
    }

    private int processErrorSendAlarms() {
        AtomicInteger integer = new AtomicInteger(0);
        querySendingAlarms().parallelStream().forEach(a -> {
            List<AlarmSendLog> alarmSendLogs = querySendingAlarmLogs(a.getAlarmId());
            if (alarmSendLogs.stream().allMatch(log -> log.getStatus().isFinishType())) {
                updateStatus(a, ApeAlarmSendStatus.已发送);
                integer.incrementAndGet();
            } else if (alarmSendLogs.stream().anyMatch(log -> log.getStatus().isErrorType())) {
                updateStatus(a, ApeAlarmSendStatus.发送失败);
                integer.incrementAndGet();
            }
        });
        return integer.get();
    }

    private void updateStatus(ApeAlarm alarm, ApeAlarmSendStatus apeAlarmSendStatus) {
        alarm.setSendStatus(apeAlarmSendStatus);
        new ApeOperationBuilder()
                .add("apeAlarm", alarm)
                .add("apeAlarmSendStatus", apeAlarmSendStatus)
                .execute(ApeAlarmOperationEnum.ApeAlarmUpdateSendStatus);
    }

    private List<AlarmSendLog> querySendingAlarmLogs(String alarmId) {
        return new ApeOperationBuilder().add("alarmId", alarmId).executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery);
    }

    private List<ApeAlarm> querySendingAlarms() {
        return new ApeOperationBuilder()
                .add("alarmSendStatusList", List.of(ApeAlarmSendStatus.准备发送, ApeAlarmSendStatus.准备重新发送, ApeAlarmSendStatus.发送中, ApeAlarmSendStatus.重新发送中))
                .add("maxAlarmTime", LocalDateTime.now().minusMinutes(3))
                .add("limit", 300)
                .executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery);
    }

}
