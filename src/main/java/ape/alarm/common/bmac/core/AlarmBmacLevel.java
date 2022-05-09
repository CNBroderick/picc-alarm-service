package ape.alarm.common.bmac.core;

import java.util.stream.Stream;

public enum AlarmBmacLevel {

    remain("提醒"), warning("警告"), fatal("致命");

    private final String levelName;

    AlarmBmacLevel(String actionName) {
        this.levelName = actionName;
    }

    public static AlarmBmacLevel parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    public String getLevelName() {
        return levelName;
    }

    @Override
    public String toString() {
        return name();
    }
}
