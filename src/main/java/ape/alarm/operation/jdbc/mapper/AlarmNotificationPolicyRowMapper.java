package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.convertor.JsonArrayAttributeConverter;
import ape.master.entity.convertor.StringToAlarmSlaTypeAttributeConvertor;

import java.sql.ResultSet;

public class AlarmNotificationPolicyRowMapper extends AbstractAlarmMapper<AlarmNotificationPolicy> {

    @Override
    public AlarmNotificationPolicy mapRow(ResultSet r) throws Exception {
        return new AlarmNotificationPolicy()
                .setId(r.getInt("d_id"))
                .setName(r.getString("d_name"))
                .setComCode(getComCode(r.getString("d_comcode")))
                .setAlarmType(new StringToAlarmSlaTypeAttributeConvertor().convertToEntityAttribute(r.getString("d_alarm_type")))
                .setUrlApp(getUrlApp(r.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(r.getString("d_ajax_app")))
                .setChannel(new JsonArrayAttributeConverter().convertToEntityAttribute(r.getString("d_channel")))
                .setEffective(r.getBoolean("d_effective"))
                .setUpdateTime(getUpdateTime(r))
                ;
    }

}
