package ape.alarm.operation.jdbc.time;

import ape.master.operation.internal.IApeOperationEnum;
import dataq.core.operation.JdbcOperation;

public enum AlarmTimeOperationEnum implements IApeOperationEnum {

    AlarmCampaignAdd(AlarmCampaignAdd.class, "告警活动添加"),
    AlarmCampaignDelete(AlarmCampaignDelete.class, "告警活动删除"),
    AlarmCampaignQuery(AlarmCampaignQuery.class, "告警活动查询"),
    AlarmCampaignUpdate(AlarmCampaignUpdate.class, "告警活动更新"),
    AlarmCampaignUpdateEffective(AlarmCampaignUpdateEffective.class, "告警活动更新有效性"),
    AlarmSpecialDayQuery(AlarmSpecialDayQuery.class, "特殊工作日查询"),
    AlarmSpecialDayAdd(AlarmSpecialDayAdd.class, "特殊日期增加"),
    AlarmSpecialDayEffectiveUpdate(AlarmSpecialDayEffectiveUpdate.class, "特殊日期有效性修改"),
    AlarmSpecialDayUpdate(AlarmSpecialDayUpdate.class, "特殊日期修改"),
    AlarmWeekDaysQuery(AlarmWeekDaysQuery.class, "普通工作日查询"),
    AlarmWeekDaysAdd(AlarmWeekDaysAdd.class, "普通工作日增加"),
    AlarmWeekDaysEffectiveUpdate(AlarmWeekDaysEffectiveUpdate.class, "普通工作日有效性修改"),
    AlarmWeekDaysUpdate(AlarmWeekDaysUpdate.class, "普通工作日修改"),
    ;

    private final Class<? extends JdbcOperation> jdbcOperationClass;
    private final String operationName;

    AlarmTimeOperationEnum(Class<? extends JdbcOperation> jdbcOperationClass, String operationName) {
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
