package ape.alarm.operation.jdbc.sla;

import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum AlarmSlaOperationEnum implements IApeOperationEnum {
    AlarmSlaQuery(AlarmSlaQuery.class, "告警阈值查询"),
    AlarmSlowSlaQuery(AlarmSlowSlaQuery.class, "性能阈值查询"),
    AlarmSlowSlaCampaignQuery(AlarmSlowSlaCampaignQuery.class, "活动性能阈值查询"),
    AlarmSlaAdd(AlarmSlaAdd.class, "告警阈值新建"),
    AlarmSlaEffectiveUpdate(AlarmSlaEffectiveUpdate.class, "告警阈值启用更新"),
    AlarmSlaUpdate(AlarmSlaUpdate.class, "告警阈值标准更新"),
    AlarmSlaInvalid(AlarmSlaInvalid.class, "告警阈值禁用"),
    AlarmSlaDeprecatedDetect(AlarmSlaDeprecatedDetect.class, "告警阈值清理过时数据"),
    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    AlarmSlaOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
        this.jdbcOperationClass = jdbcOperationClass;
        this.operationName = operationName;
    }

    @Override
    public String getOperationName() {
        return operationName;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public Class<? extends JdbcOperation> getJdbcOperationClass() {
        return jdbcOperationClass;
    }
}
