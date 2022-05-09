package ape.alarm.entity.alarm;

import ape.master.entity.alarm.sla.AlarmSlaType;
import ape.master.operation.ApeConnectionManager;
import dataq.core.data.schema.Schema;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.bklab.quark.util.schema.SchemaFactory;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmDataFieldTranslator {

    private static AlarmDataFieldTranslator instance;
    private Map<String, Map<String, AlarmDataFieldMapping>> dataMap;
    private LocalDateTime updateTime = LocalDateTime.MIN;


    public static AlarmDataFieldTranslator getInstance() {
        if (instance == null) instance = new AlarmDataFieldTranslator().reload();
        if (instance.dataMap == null) instance.reload();
        return instance.reload();
    }

    public AlarmDataFieldTranslator reload() {
        return updateTime.plusSeconds(5).isBefore(LocalDateTime.now()) ? forceReload() : this;
    }

    public AlarmDataFieldTranslator forceReload() {
        try {
            Schema schema = new SchemaFactory().string("type", "name", "caption").get();
            List<AlarmDataFieldMapping> mappings = new PreparedStatementHelper(ApeConnectionManager.getInstance().getMaster(), """
                    SELECT * FROM `tb_alarm_data_field_mapping`
                    """).executeQuery().asList(new AlarmDataFieldMapping.Mapper());

            this.dataMap = mappings.stream().collect(Collectors.groupingBy(AlarmDataFieldMapping::getType,
                    Collectors.toMap(AlarmDataFieldMapping::getName, Function.identity())));
            this.updateTime = LocalDateTime.now();
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("加载失败", e);
        }
        return this;
    }

    public String getCaption(AlarmSlaType alarmSlaType, String name) {
        return getCaption(alarmSlaType.name(), name);
    }

    public AlarmDataFieldMapping get(AlarmSlaType alarmSlaType, String name) {
        return get(alarmSlaType.name(), name);
    }

    public AlarmDataFieldMapping get(String alarmSlaType, String name) {
        AlarmDataFieldMapping mapping = dataMap.getOrDefault(alarmSlaType, Collections.emptyMap()).get(name);
        if (mapping == null) {
            mapping = new AlarmDataFieldMapping(alarmSlaType, name, name, "");
            dataMap.computeIfAbsent(alarmSlaType, a -> new LinkedHashMap<>()).put(name, mapping);
        }
        return mapping;
    }

    public String getCaption(String alarmSlaType, String name) {
        return get(alarmSlaType, name).getCaption();
    }

    public String getSuffix(String alarmSlaType, String name) {
        return get(alarmSlaType, name).getSuffix();
    }

}
