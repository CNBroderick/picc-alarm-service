package ape.alarm.operation.jdbc.transmission;

import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum TransmissionOperationEnum implements IApeOperationEnum {
    AlarmContactAdd(AlarmContactAdd.class, "告警联系人添加"),
    AlarmContactDelete(AlarmContactDelete.class, "告警联系人删除"),
    AlarmContactQuery(AlarmContactQuery.class, "告警联系人查询"),
    AlarmContactUpdate(AlarmContactUpdate.class, "告警联系人修改"),

    AlarmNotificationPolicyAdd(AlarmNotificationPolicyAdd.class, "告警通知策略添加"),
    AlarmNotificationPolicyDelete(AlarmNotificationPolicyDelete.class, "告警通知策略删除"),
    AlarmNotificationPolicyUpdate(AlarmNotificationPolicyUpdate.class, "告警通知策略修改"),

    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    TransmissionOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
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
