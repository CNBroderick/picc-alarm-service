package ape.alarm.common.email.sender;

import ape.alarm.common.core.IAlarmSender;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.master.service.common.ApeServiceProvider;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public interface IAlarmEmailSender extends IAlarmSender {

    String ALARM_EMAIL_SEND_QUEUE_KEY_NAME = "APE-ALARM-EMAIL-SEND-QUEUE";

    boolean resend(AlarmSendLog alarmSendLog);

    boolean sendRestored(List<AlarmSendLog> sources);

    default void addSendingQueue(Set<Integer> alarmSendLogIds) {
        ApeServiceProvider.getInstance().redisStringService().redisStringTemplate().opsForSet()
                .add(ALARM_EMAIL_SEND_QUEUE_KEY_NAME, alarmSendLogIds.stream().map(String::valueOf).toArray(String[]::new));
    }

    default void addSendingQueue(AlarmSendLog alarmSendLog) {
        LoggerFactory.getLogger(getClass()).info("添加邮件到发送队列：%s --> %s[%s]。".formatted(
                alarmSendLog.getTitle(),
                alarmSendLog.getContactName(),
                alarmSendLog.getAddress()
        ));
        ApeServiceProvider.getInstance().redisStringService().redisStringTemplate()
                .opsForSet().add(ALARM_EMAIL_SEND_QUEUE_KEY_NAME, "" + alarmSendLog.getId());
    }
}
