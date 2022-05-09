package ape.alarm.common.email.scanner;


import ape.alarm.common.email.sender.AlarmEmailSender;
import ape.alarm.common.email.sender.IAlarmEmailSender;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarmSendStatus;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.parameter.ParameterEnum;
import ape.master.service.common.ApeServiceProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.text.StringExtractor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class ApeAlarmMailScheduledScanner {

    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警邮件发送后台服务, ParameterEnum.后台服务.告警邮件发送后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;
    private static long SEND_ALARMS_COUNT = 0;
    private static List<AlarmSendLog> currentSend;

    @Scheduled(cron = "*/5 * * * * ?")
    public void execute() {
        if (!ABILITY_UTIL.executable()) return;

        if (IS_RUNNING) {
            log.debug("处理APE告警发送队列尚未完成，停止本次运行。");
            return;
        }
        log.debug("APE告警发送队列：开始运行。");
        IS_RUNNING = Boolean.TRUE;
        try {
            ABILITY_UTIL.writeRunning();
            sendQueueAlarm();
        } catch (Exception e) {
            log.error("处理APE告警发送队列失败。", e);
        } finally {
            IS_RUNNING = Boolean.FALSE;
            ABILITY_UTIL.writeRunTime();
            log.debug("APE告警发送队列：结束运行。");
        }
    }

    private void sendQueueAlarm() {
        List<String> ids = ApeServiceProvider.getInstance().redisStringService().redisStringTemplate()
                .opsForSet().pop(IAlarmEmailSender.ALARM_EMAIL_SEND_QUEUE_KEY_NAME, 512);

        if (ids == null || ids.isEmpty()) return;
        Set<Integer> set = ids.stream().map(StringExtractor::parseInt).filter(a -> a > 0).collect(Collectors.toSet());
        List<AlarmSendLog> alarmSendLogs = new ApeOperationBuilder().add("ids", set)
                .executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery);
        currentSend = alarmSendLogs;
        Set<String> processed = new LinkedHashSet<>();
        alarmSendLogs.parallelStream().map(AlarmEmailSender::new).forEach(a -> {
            if (a.send()) {
                processed.add(a.getAlarmSendLog().getAlarmId());
                SEND_ALARMS_COUNT++;
                log.debug("APE告警发送队列：成功发送邮件%s到%s[%s]。".formatted(
                        a.getAlarmSendLog().getTitle(),
                        a.getAlarmSendLog().getContactName(),
                        a.getAlarmSendLog().getAddress()
                ));
            } else {
                ApeServiceProvider.getInstance().redisStringService().redisStringTemplate()
                        .opsForSet().add(IAlarmEmailSender.ALARM_EMAIL_SEND_QUEUE_KEY_NAME, "" + a.getAlarmSendLog().getId());
                log.debug("APE告警发送队列：发送邮件%s到%s[%s]失败。".formatted(
                        a.getAlarmSendLog().getTitle(),
                        a.getAlarmSendLog().getContactName(),
                        a.getAlarmSendLog().getAddress()
                ));
            }
        });

        Map<String, List<AlarmSendLog>> map = new ApeOperationBuilder().add("alarmIds", processed)
                .<AlarmSendLog>executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery).stream().collect(Collectors
                        .groupingBy(AlarmSendLog::getAlarmId, Collectors.mapping(Function.identity(), Collectors.toList())));

        Set<String> finished = map.entrySet().stream().filter(entry -> entry.getValue().stream().allMatch(a -> a.getStatus().isFinishType()))
                .map(Map.Entry::getKey).collect(Collectors.toSet());
        if (!finished.isEmpty()) {
            new ApeOperationBuilder()
                    .add("alarmIds", finished)
                    .add("apeAlarmSendStatus", ApeAlarmSendStatus.已发送)
                    .execute(ApeAlarmOperationEnum.ApeAlarmUpdateSendStatusByAlarmId);
        }
    }
}
