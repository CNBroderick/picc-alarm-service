package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.alarm.ApeAlarm;

import java.sql.ResultSet;

public class ApeAlarmRowMapper extends AbstractAlarmMapper<ApeAlarm> {

    public ApeAlarmRowMapper() {

    }

    @Override
    public ApeAlarm mapRow(ResultSet r) throws Exception {
        return new ApeAlarm()
                .setId(r.getInt("d_id"))
                .setAlarmId(r.getString("d_alarm_id"))
                .setComCode(getComCode(r))
                .setAlarmType(r.getString("d_alarm_type"))
                .setAlarmTime(getLocalDateTime(r, "d_alarm_time"))
                .setStartTime(getLocalDateTime(r, "d_start_time"))
                .setEndTime(getLocalDateTime(r, "d_end_time"))
                .setUrlApp(getUrlApp(r))
                .setUrl(r.getString("d_url"))
                .setAjaxApp(getAjaxApp(r))
                .setAjaxUrl(r.getString("d_ajax_url"))
                .setData(r.getString("d_data"))
                .setSendTime(getLocalDateTime(r, "d_send_time"))
                .setSendStatus(r.getString("d_send_status"))
                .setRestoredTime(getLocalDateTime(r, "d_restored_time"))
                ;
    }

}
