package ape.alarm.common.core;

import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.entity.alarm.transmission.AlarmContactChannelTypeEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationBinding;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.service.common.ApeServiceProvider;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface IAlarmSender {

    boolean send(List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm);

    default Set<AlarmNotificationBinding> getAlarmContacts(List<AlarmNotificationPolicy> notificationPolicies, AlarmContactChannelTypeEnum channel) {
        Set<AlarmNotificationBinding> bindings = ApeServiceProvider.getInstance().alarmNotificationService().findAllBindings(notificationPolicies);
        if (bindings == null || bindings.isEmpty()) return Collections.emptySet();
        return bindings.stream().filter(b -> b.supportChannel(channel)).collect(Collectors.toSet());
    }

    default void save(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogAdd);
    }

    default void update(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogUpdate);
    }

    default void updateStatus(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogUpdateStatus);
    }
}
