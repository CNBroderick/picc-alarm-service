package ape.alarm.entity.alarm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public enum ApeAlarmSendStatus {
    待发送, 准备发送, 发送中, 已发送, 已处理, 发送失败, 准备重新发送, 重新发送中, 重新发送失败, 其他;

    public static ApeAlarmSendStatus parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(其他);
    }

    public static Map<String, List<ApeAlarmSendStatus>> createStatusMap() {
        Map<String, List<ApeAlarmSendStatus>> map = new LinkedHashMap<>();
        map.put("待发送", List.of(待发送, 准备发送, 准备重新发送));
        map.put("发送中", List.of(发送中, 重新发送中));
        map.put("发送成功", List.of(已发送, 已处理));
        map.put("发送失败", List.of(发送失败, 重新发送失败, 其他));
        return map;
    }

    @Override
    public String toString() {
        return name();
    }
}
