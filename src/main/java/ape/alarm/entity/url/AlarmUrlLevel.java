package ape.alarm.entity.url;

import java.util.stream.Stream;

public enum AlarmUrlLevel {

    ROOT(0, "根"),
    PROVINCE(1, "省级"),
    URL_APP(2, "URL应用"),
    URL(3, "URL"),
    AJAX_APP(4, "AJAX应用"),
    TIME(5, "时间"),
    ;

    public final int level;
    public final String caption;

    AlarmUrlLevel(int level, String caption) {
        this.level = level;
        this.caption = caption;
    }

    public static AlarmUrlLevel parse(AlarmUrl alarmUrl) {
        if (alarmUrl.getStartTime() != null || alarmUrl.getEndTime() != null) return TIME;
        if (alarmUrl.getAjaxApp() != null) return AJAX_APP;
        if (alarmUrl.getUrl() != null) return URL;
        if (alarmUrl.getUrlApp() != null) return URL_APP;
        if (alarmUrl.getComCode() != null) return PROVINCE;
        return ROOT;
    }

    public static AlarmUrlLevel parse(String name) {
        return Stream.of(values()).filter(e -> e.name().equals(name)).findFirst().orElse(null);
    }

    public static AlarmUrlLevel parse(int level) {
        return Stream.of(values()).filter(e -> e.level == level).findFirst().orElse(null);
    }

    public AlarmUrlLevel childLevel() {
        return parse(level + 1);
    }

    @Override
    public String toString() {
        return name();
    }
}
