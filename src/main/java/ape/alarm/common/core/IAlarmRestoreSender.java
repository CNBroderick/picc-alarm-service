package ape.alarm.common.core;

import ape.alarm.entity.alarm.ApeAlarm;

public interface IAlarmRestoreSender extends IAlarmSender {

    boolean restoredAlarm(ApeAlarm alarm);
}
