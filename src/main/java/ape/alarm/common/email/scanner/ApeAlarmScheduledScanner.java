package ape.alarm.common.email.scanner;

import ape.alarm.common.core.AlarmSenderManager;
import ape.alarm.common.email.consumer.AlarmNotificationPolicyManager;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmSendStatus;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bklab.quark.util.number.DigitalFormatter;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.bklab.quark.util.time.RunningTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Configurable
@EnableScheduling
public class ApeAlarmScheduledScanner {
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警邮件生成后台服务, ParameterEnum.后台服务.告警邮件生成后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;
    private static long SEND_ALARMS_COUNT = 0;
    private static long RESEND_ALARM_COUNT = 0;
    private static List<ApeAlarm> currentSend;
    private static Map<ApeAlarm, List<AlarmSendLog>> currentResend;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static JsonObject getStatus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("启用状态", ABILITY_UTIL.executable() ? "已启用" : "已禁用");
        jsonObject.addProperty("运行状态", IS_RUNNING ? "正在运行" : "未启动");
        jsonObject.addProperty("最后运行时间", ABILITY_UTIL.getRunTime());
        jsonObject.addProperty("发送告警次数", new DigitalFormatter(SEND_ALARMS_COUNT).toInteger());
        jsonObject.addProperty("重发告警次数", new DigitalFormatter(RESEND_ALARM_COUNT).toInteger());

        JsonObject data = new JsonObject();
        if (currentSend != null) data.add("发送告警", new Gson().toJsonTree(currentSend));
        if (currentResend != null) data.add("重发告警", new Gson().toJsonTree(currentResend));
        jsonObject.add("正在处理", data);

        return jsonObject;
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void execute() {
        if (IS_RUNNING) {
            logger.info("APE告警发送服务尚未运行完毕，停止本次运行。");
            return;
        }
        if (!ABILITY_UTIL.executable()) return;
        try {
            ABILITY_UTIL.writeRunning();
            IS_RUNNING = true;
            RunningTime runningTime = new RunningTime();
            ParameterEnum.告警系统.告警系统运行状态.setValue("正在运行");
            int id = printAlive();
            int sendAlarms = processWaitingSendAlarms();
            int errorSendAlarms = processErrorSendAlarms();
            int waitingSendAlarms = processWaitingSendAlarmLogs();

            ApeAlarmScheduledScanner.SEND_ALARMS_COUNT += sendAlarms;
            ApeAlarmScheduledScanner.RESEND_ALARM_COUNT += errorSendAlarms;
            logger.info("APE告警发送服务发送完毕，执行ID = {}，线程ID = {}，发送告警 = {}条，重发告警 = {}条，重新加入邮件队列 = {}条，用时 = {}。",
                    id, Thread.currentThread().getId(), sendAlarms, errorSendAlarms, waitingSendAlarms, runningTime.time());

        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("APE告警发送服务发生错误。", e);
        } finally {
            ABILITY_UTIL.writeRunTime();
            IS_RUNNING = false;
            currentSend = null;
            currentResend = null;
            ParameterEnum.告警系统.告警系统运行状态.setValue("等待运行");
        }
    }

    private int printAlive() {
        int id = ParameterEnum.告警系统.告警系统后台运行id.getEntryValue(0) + 1;
        ParameterEnum.告警系统.告警系统后台运行id.setValue(id + "");
        ParameterEnum.告警系统.告警系统最后一次运行时间.setValue(LocalDateTimeFormatter.Short(LocalDateTime.now()));
        logger.info("APE告警发送服务开始运行，执行ID = {}，线程ID = {}。", id, Thread.currentThread().getId());
        return id;
    }

    private int processWaitingSendAlarms() {
        List<ApeAlarm> alarms = queryWaitingSendAlarms();
        currentSend = alarms;
        updateStatus(alarms, ApeAlarmSendStatus.准备发送);
        for (ApeAlarm alarm : alarms) {
            List<AlarmNotificationPolicy> notificationPolicies = AlarmNotificationPolicyManager.getInstance().getAlarmNotificationPolicies(alarm);
            updateStatus(alarm, ApeAlarmSendStatus.发送中);
            boolean result = AlarmSenderManager.getInstance().send(notificationPolicies, alarm);
//            updateStatus(alarm, result ? ApeAlarmSendStatus.已发送 : ApeAlarmSendStatus.发送失败);
            if (new ApeOperationBuilder().add("alarmId", alarm.getAlarmId())
                    .executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery).isEmpty()) {
                updateStatus(alarm, ApeAlarmSendStatus.已处理);
            }

        }
        return alarms.size();
    }

    private int processErrorSendAlarms() {
        List<AlarmSendLog> alarmSendLogs = queryErrorSendAlarms();
        Map<ApeAlarm, List<AlarmSendLog>> map = alarmSendLogs.stream().filter(alarmSendLog -> alarmSendLog.getAlarm() != null)
                .collect(Collectors.groupingBy(AlarmSendLog::getAlarm, Collectors.mapping(Function.identity(), Collectors.toList())));
        currentResend = map;
        updateStatus(map.keySet(), ApeAlarmSendStatus.准备重新发送);
        updateStatus(alarmSendLogs, AlarmSendStatus.准备重新发送);
        processAlarmSendLog(map);
        return map.size();
    }

    private int processWaitingSendAlarmLogs() {
        List<AlarmSendLog> alarmSendLogs = queryWaitingSendAlarmLogs();
        Map<ApeAlarm, List<AlarmSendLog>> map = alarmSendLogs.stream().filter(alarmSendLog -> alarmSendLog.getAlarm() != null)
                .collect(Collectors.groupingBy(AlarmSendLog::getAlarm, Collectors.mapping(Function.identity(), Collectors.toList())));
        currentResend = map;
        updateStatus(map.keySet(), ApeAlarmSendStatus.准备发送);
        updateStatus(alarmSendLogs, AlarmSendStatus.准备发送);

        processAlarmSendLog(map);
        return map.size();
    }

    private void processAlarmSendLog(Map<ApeAlarm, List<AlarmSendLog>> map) {
        for (Map.Entry<ApeAlarm, List<AlarmSendLog>> entry : map.entrySet()) {
            ApeAlarm alarm = entry.getKey();
            boolean success = true;
            for (AlarmSendLog sendLog : entry.getValue()) {
                success &= AlarmSenderManager.getInstance().resend(sendLog);
            }
//            updateStatus(alarm, success ? ApeAlarmSendStatus.已发送 : ApeAlarmSendStatus.重新发送失败);
        }
    }

    private void updateStatus(Collection<ApeAlarm> apeAlarms, ApeAlarmSendStatus apeAlarmSendStatus) {
        apeAlarms.forEach(alarm -> alarm.setSendStatus(apeAlarmSendStatus));
        new ApeOperationBuilder()
                .add("apeAlarms", apeAlarms)
                .add("apeAlarmSendStatus", apeAlarmSendStatus)
                .execute(ApeAlarmOperationEnum.ApeAlarmUpdateSendStatus);
    }

    private void updateStatus(Collection<AlarmSendLog> alarmSendLogs, AlarmSendStatus apeAlarmSendStatus) {
        alarmSendLogs.forEach(alarm -> alarm.setStatus(apeAlarmSendStatus));
        new ApeOperationBuilder()
                .add("alarmSendLogs", alarmSendLogs)
                .execute(ApeAlarmOperationEnum.AlarmSendLogUpdateStatus);
    }

    private void updateStatus(ApeAlarm alarm, ApeAlarmSendStatus apeAlarmSendStatus) {
        alarm.setSendStatus(apeAlarmSendStatus);
        new ApeOperationBuilder()
                .add("apeAlarm", alarm)
                .add("apeAlarmSendStatus", apeAlarmSendStatus)
                .execute(ApeAlarmOperationEnum.ApeAlarmUpdateSendStatus);
    }

    private List<ApeAlarm> queryWaitingSendAlarms() {
        return new ApeOperationBuilder()
                .add("waitingSendAlarms", Boolean.TRUE)
                .add("limit", 300)
                .executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery);
    }

    private List<AlarmSendLog> queryErrorSendAlarms() {
        return new ApeOperationBuilder()
                .add("alarmSendStatusList", List.of(AlarmSendStatus.发送失败, AlarmSendStatus.重新发送失败))
                .add("limit", 300)
                .executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery);
    }

    private List<AlarmSendLog> queryWaitingSendAlarmLogs() {
        return new ApeOperationBuilder()
                .add("alarmSendStatusList", List.of(AlarmSendStatus.待发送, AlarmSendStatus.发送中, AlarmSendStatus.重新发送中))
                .add("maxCreateTime", LocalDateTime.now().minusMinutes(2))
                .add("limit", 300)
                .executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery);
    }

}
