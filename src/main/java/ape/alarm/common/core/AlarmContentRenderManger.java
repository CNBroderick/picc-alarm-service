package ape.alarm.common.core;

import ape.alarm.common.email.builder.*;
import ape.master.entity.alarm.sla.AlarmSlaType;

import java.util.LinkedHashMap;
import java.util.Map;

import static ape.master.entity.alarm.sla.AlarmSlaType.*;

public class AlarmContentRenderManger {

    private static AlarmContentRenderManger instance;

    private final Map<AlarmSlaType, AbstractAlarmEmailBuilder> alarmEmailBuilderMap = new LinkedHashMap<>();

    private AlarmContentRenderManger() {
    }

    public static AlarmContentRenderManger getInstance() {
        if (instance == null) instance = new AlarmContentRenderManger()
                .add(页面加载时间, new AlarmEmailPageLoadingBuilder())
                .add(URL响应时间, new AlarmEmailUrlResponseBuilder())
                .add(AJAX性能, new AlarmEmailAjaxUrlSlowBuilder())
                .add(AJAX错误, new AlarmEmailAjaxErrorBuilder())
                .add(JS错误, new AlarmEmailJsErrorBuilder())
                ;
        return instance;
    }

    public AbstractAlarmEmailBuilder get(AlarmSlaType alarmSlaType) {
        return alarmEmailBuilderMap.get(alarmSlaType);
    }

    public AlarmContentRenderManger add(AlarmSlaType alarmSlaType, AbstractAlarmEmailBuilder builder) {
        alarmEmailBuilderMap.put(alarmSlaType, builder);
        return this;
    }

    public Map<AlarmSlaType, AbstractAlarmEmailBuilder> getAlarmEmailBuilderMap() {
        return alarmEmailBuilderMap;
    }
}
