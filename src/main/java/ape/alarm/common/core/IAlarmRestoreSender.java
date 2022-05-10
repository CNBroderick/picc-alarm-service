package ape.alarm.common.core;

import java.time.LocalDateTime;

public interface IAlarmRestoreSender extends IAlarmSender {

    boolean restoredAlarm(int alarmId, LocalDateTime restoreTime);
}
