package ape.alarm.common.bmac.core;

import java.util.stream.Stream;

public enum AlarmBmacAction {

    firing("产生告警"),resolved("消除告警");

    private final String actionName;

    AlarmBmacAction(String actionName) {
        this.actionName = actionName;
    }

    public static AlarmBmacAction parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public String toString() {
        return name();
    }
}
