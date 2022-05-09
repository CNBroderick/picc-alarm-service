package ape.alarm.entity.transmission;

import java.util.stream.Stream;

public enum AlarmContactChannelTypeEnum {

    邮件,
    短信,
    企业微信,
    工单,

    ;

    public static AlarmContactChannelTypeEnum parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return name();
    }
}
