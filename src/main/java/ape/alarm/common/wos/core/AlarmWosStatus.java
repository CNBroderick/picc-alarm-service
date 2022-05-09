package ape.alarm.common.wos.core;

import java.util.stream.Stream;

public enum AlarmWosStatus {
    待创建, 创建失败, 已创建, 已关闭,
    ;

    public static AlarmWosStatus parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return name();
    }
}
