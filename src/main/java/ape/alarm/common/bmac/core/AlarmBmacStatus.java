package ape.alarm.common.bmac.core;

import java.util.stream.Stream;

public enum AlarmBmacStatus {
    待创建, 创建失败, 已创建, 已关闭, 关闭失败,
    ;

    public static AlarmBmacStatus parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return name();
    }
}
