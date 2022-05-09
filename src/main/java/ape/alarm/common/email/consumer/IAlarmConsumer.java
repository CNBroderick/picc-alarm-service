package ape.alarm.common.email.consumer;

import ape.alarm.entity.alarm.ApeAlarm;

import java.util.function.Consumer;

public interface IAlarmConsumer extends Consumer<ApeAlarm> {

    boolean isSupport(ApeAlarm alarm);

    boolean send(ApeAlarm alarm);

    @Override
    default void accept(ApeAlarm alarm) {
        send(alarm);
    }
}
