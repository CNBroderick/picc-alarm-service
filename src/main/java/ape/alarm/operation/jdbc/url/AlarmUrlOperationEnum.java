package ape.alarm.operation.jdbc.url;

import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum AlarmUrlOperationEnum implements IApeOperationEnum {
    AlarmUrlAdd(AlarmUrlAdd.class, "关键URL添加"),
    AlarmUrlDelete(AlarmUrlDelete.class, "关键URL删除"),
    AlarmUrlQuery(AlarmUrlQuery.class, "关键URL查询"),
    AlarmUrlUpdate(AlarmUrlUpdate.class, "关键URL更新"),
    AlarmUrlUpdateAlarmStatus(AlarmUrlUpdateAlarmStatus.class, "关键URL更新告警状态"),
    AlarmUrlUpdateEffective(AlarmUrlUpdateEffective.class, "关键URL更新生效状态"),
    AlarmUrlDeprecatedDetect(AlarmUrlDeprecatedDetect.class, "关键URL重新更新弃用状态"),
    AlarmUrlUpdateError(AlarmUrlUpdateError.class, "关键URL更新错误"),
    AlarmUrlExistBatchQuery(AlarmUrlExistBatchQuery.class, "关键URL ID批量查询"),
    AlarmUrlInvalid(AlarmUrlInvalid.class, "关键URL设置失效"),
    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    AlarmUrlOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
        this.jdbcOperationClass = jdbcOperationClass;
        this.operationName = operationName;
    }

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
