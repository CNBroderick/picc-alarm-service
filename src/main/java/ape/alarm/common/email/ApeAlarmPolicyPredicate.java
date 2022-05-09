package ape.alarm.common.email;

import ape.alarm.entity.alarm.ApeAlarm;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.code.GeneralVariable;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class ApeAlarmPolicyPredicate implements BiPredicate<AlarmNotificationPolicy, ApeAlarm> {

    @Override
    public boolean test(AlarmNotificationPolicy policy, ApeAlarm alarm) {
        if (!policy.isEffective()) return false;
        if (Stream.of(alarm.getComCodeId(), GeneralVariable.COMCODE).noneMatch(a -> Objects.equals(a, policy.getComcode()))) return false;
        if (policy.getAlarmType() != null && alarm.getAlarmType() != policy.getAlarmType()) return false;
        if (Stream.of(alarm.getUrlAppId(), GeneralVariable.URL_APP).noneMatch(a -> Objects.equals(a, policy.getUrlAppcode()))) return false;
        if (alarm.getAlarmType().hasAjax()
            && (policy.getAlarmType() != null && policy.getAlarmType().hasAjax())
            && Stream.of(alarm.getAjaxAppId(), GeneralVariable.AJAX_APP).noneMatch(a -> Objects.equals(a, policy.getAjaxAppcode()))
        ) return false;

        return policy.isEffective();
    }

}
