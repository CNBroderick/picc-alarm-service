package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.alarm.ApeAlarmDetail;

import java.sql.ResultSet;

public class ApeAlarmDetailRowMapper extends AbstractAlarmMapper<ApeAlarmDetail> {

    public ApeAlarmDetailRowMapper() {

    }

    @Override
    public ApeAlarmDetail mapRow(ResultSet r) throws Exception {
        return new ApeAlarmDetail()
                .setId(r.getInt("d_id"))
                .setAid(r.getInt("d_aid"))
                .setAlarmId(r.getString("d_alarm_id"))
                .setComCode(getComCode(r.getString("d_comcode")))
                .setUrlApp(getUrlApp(r.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(r.getString("d_ajax_app")))
                .setAlarmType(r.getString("d_alarm_type"))
                .setAlarmTime(getLocalDateTime(r, "d_alarm_time"))
                .setStartTime(getLocalDateTime(r, "d_start_time"))
                .setEndTime(getLocalDateTime(r, "d_end_time"))
                .setDetailTime(getLocalDateTime(r, "d_detail_time"))
                .setUserCode(r.getString("d_user_code"))
                .setIp(r.getString("d_ip"))
                .setUrl(r.getString("d_url"))
                .setAjaxUrl(r.getString("d_ajax_url"))
                .setData(r.getString("d_data"))
                ;
    }

}
