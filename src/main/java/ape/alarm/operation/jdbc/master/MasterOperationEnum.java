package ape.alarm.operation.jdbc.master;


import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum MasterOperationEnum implements IApeOperationEnum {
    AppCodeQuery(AppCodeQuery.class, "应用代码查询"),
    ComCodeQuery(ComCodeQuery.class, "机构代码查询"),
    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    MasterOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
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
