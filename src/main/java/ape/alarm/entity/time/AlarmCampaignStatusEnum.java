package ape.alarm.entity.time;

import java.util.stream.Stream;

public enum AlarmCampaignStatusEnum {
    新建(0),
    开始(1),
    处理中(2),
    完成(3),
    其他(4),
    ;
    private final int code;

    AlarmCampaignStatusEnum(int code) {
        this.code = code;
    }


    public static AlarmCampaignStatusEnum parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    public static AlarmCampaignStatusEnum parse(int code) {
        return Stream.of(values()).filter(e -> e.code == code).findFirst().orElse(其他);
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return name() + "[" + code + "]";
    }
}
