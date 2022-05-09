package ape.alarm.entity.sla;

import java.util.stream.Stream;

public enum AlarmSlowSlaType {

    页面加载时间,
    URL响应时间,
    AJAX响应时间,

    ;

    public static AlarmSlowSlaType parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return name();
    }
}
