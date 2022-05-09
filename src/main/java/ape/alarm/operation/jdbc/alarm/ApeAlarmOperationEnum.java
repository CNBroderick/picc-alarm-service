package ape.alarm.operation.jdbc.alarm;

import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum ApeAlarmOperationEnum implements IApeOperationEnum {
    ApeAlarmQuery(ApeAlarmQuery.class, "告警查询"),
    ApeAlarmDetailQuery(ApeAlarmDetailQuery.class, "告警详细列表查询"),
    ApeAlarmUpdate(ApeAlarmUpdate.class, "告警更新"),
    ApeAlarmUpdateSendStatus(ApeAlarmUpdateSendStatus.class, "告警更新发送状态"),
    AlarmSendLogAdd(AlarmSendLogAdd.class, "告警发送日志增加"),
    AlarmNearlyExistQuery(AlarmNearlyExistQuery.class, "相同告警查询"),
    AlarmSendLogQuery(AlarmSendLogQuery.class, "告警发送日志查询"),
    AlarmSendLogUpdate(AlarmSendLogUpdate.class, "告警发送日志更新"),
    AlarmSendLogUpdateStatus(AlarmSendLogUpdateStatus.class, "告警发送日志更新状态"),
    ApeAlarmUpdateSendStatusByAlarmId(ApeAlarmUpdateSendStatusByAlarmId.class, "告警发送日志更新状态"),
    AlarmSendLogClearError(AlarmSendLogClearError.class, "告警发送日志清除错误"),
    UrlFrontPageMappingQuery(UrlFrontPageMappingQuery.class, "前台页面名称对照表查询"),
    UrlMicroServiceMappingQuery(UrlMicroServiceMappingQuery.class, "前台微服务名称对照表查询"),
    ApeAlarmRestoredUpdateByMap(ApeAlarmRestoredUpdateByMap.class, "告警恢复更新"),
    ApeAlarmNotRestoredAlarmIdQuery(ApeAlarmNotRestoredAlarmIdQuery.class, "告警未回复ID查询"),
    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    ApeAlarmOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
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
