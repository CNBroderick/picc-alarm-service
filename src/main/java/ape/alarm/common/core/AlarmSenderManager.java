package ape.alarm.common.core;

import ape.alarm.common.bmac.AlarmBmacSender;
import ape.alarm.common.email.sender.AlarmSendLogBuilder;
import ape.alarm.common.email.sender.IAlarmEmailSender;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.common.wos.AlarmWosSender;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.entity.alarm.transmission.AlarmContactChannelTypeEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class AlarmSenderManager {

    private static AlarmSenderManager instance;

    private final Map<AlarmContactChannelTypeEnum, IAlarmSender> alarmSenderMap = new LinkedHashMap<>();

    public AlarmSenderManager() {
        alarmSenderMap.put(AlarmContactChannelTypeEnum.邮件, new AlarmSendLogBuilder());
        alarmSenderMap.put(AlarmContactChannelTypeEnum.工单, new AlarmWosSender());
        alarmSenderMap.put(AlarmContactChannelTypeEnum.蓝鲸告警中心, new AlarmBmacSender());
    }

    public static AlarmSenderManager getInstance() {
        if (instance == null) instance = new AlarmSenderManager();
        return instance;
    }

    public boolean send(List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm) {
        boolean success = true;
        for (AlarmContactChannelTypeEnum channelTypeEnum : alarmSenderMap.keySet()) {
            try {
                success &= send(channelTypeEnum, notificationPolicies, alarm);
            } catch (Exception exception) {
                LoggerFactory.getLogger(getClass()).error("发送" + channelTypeEnum.name() + "失败", exception);
            }
        }
        return success;
    }

    public boolean send(AlarmContactChannelTypeEnum channel, List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm) {
        if (channel == null) return true;
        IAlarmSender sender = alarmSenderMap.get(channel);
        if (sender == null) throw new UnsupportedOperationException("暂不支持[" + channel.name() + "]渠道的发送");
        try {
            log.info("开始发送告警" + channel.name() + "。告警id=" + alarm.getId());
            return sender.send(notificationPolicies, alarm);
        } catch (Exception e) {
            log.error("发送告警" + channel.name() + "失败。告警id=" + alarm.getId(), e);
            return false;
        } finally {
            log.info("处理发送告警" + channel.name() + "。告警id=" + alarm.getId());
        }
    }

    public boolean sendRestore(List<AlarmSendLog> sources) {
        Map<AlarmContactChannelTypeEnum, List<AlarmSendLog>> map = sources.stream().collect(Collectors
                .groupingBy(AlarmSendLog::getChannel, Collectors.mapping(Function.identity(), Collectors.toList())));

        Set<String> unsupported = new LinkedHashSet<>();
        for (Map.Entry<AlarmContactChannelTypeEnum, List<AlarmSendLog>> entry : map.entrySet()) {
            if (entry.getKey() == null) continue;

            IAlarmSender sender = alarmSenderMap.get(entry.getKey());
            if (sender instanceof IAlarmEmailSender) {
                ((IAlarmEmailSender) sender).sendRestored(entry.getValue());
            } else if (sender instanceof IAlarmRestoreSender) {
                Collection<ApeAlarm> alarms = entry.getValue().stream()
                        .filter(a -> a.getAid() > 0 && a.getAlarm() != null)
                        .collect(Collectors.toMap(AlarmSendLog::getAid, AlarmSendLog::getAlarm, (a, b) -> b, LinkedHashMap::new)).values();
                alarms.forEach(a -> {
                    try {
                        if (((IAlarmRestoreSender) sender).restoredAlarm(a)) {
                            log.info("恢复渠道[" + entry.getKey().name() + "]告警[alarm_id=" + a.getId() + "]成功。");
                        } else {
                            log.error("恢复渠道[" + entry.getKey().name() + "]告警[alarm_id=" + a.getId() + "]失败。");
                        }
                    } catch (Exception e) {
                        log.error("恢复渠道[" + entry.getKey().name() + "]告警[alarm_id=" + a.getId() + "]出错，原因：" + e.getMessage(), e);
                    }
                });
            }

            unsupported.add(entry.getKey().name());
        }

        if (!unsupported.isEmpty()) {
            throw new UnsupportedOperationException("告警恢复暂不支持渠道[" + String.join("、", unsupported) + "]的发送");
        }

        return false;
    }

    public boolean resend(AlarmSendLog alarmSendLog) {
        update(alarmSendLog.setStatus(AlarmSendStatus.发送中));
        AlarmContactChannelTypeEnum channel = alarmSendLog.getChannel();
        if (channel == null) {
            update(alarmSendLog.setErrorMessage("未标记发送渠道。").setResend(true).setFailedSendStatus());
            return false;
        }

        IAlarmSender sender = alarmSenderMap.get(channel);
        if (sender instanceof IAlarmEmailSender) {
            return ((IAlarmEmailSender) sender).resend(alarmSendLog);
        }

        String message = "暂不支持[" + channel.name() + "]渠道的发送";
        UnsupportedOperationException exception = new UnsupportedOperationException(message);
        LoggerFactory.getLogger(getClass()).error(message, exception);
        update(alarmSendLog.setErrorMessage("暂不支持[" + channel.name() + "]渠道的发送").setResend(true).setFailedSendStatus().setErrorStack(exception));
        return false;
    }

    private void update(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogUpdateStatus);
    }
}
