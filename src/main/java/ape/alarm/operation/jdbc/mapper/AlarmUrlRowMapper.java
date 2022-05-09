package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.url.AlarmUrl;

import java.sql.ResultSet;

public class AlarmUrlRowMapper extends AbstractAlarmMapper<AlarmUrl> {

    @Override
    public AlarmUrl mapRow(ResultSet resultSet) throws Exception {
        return new AlarmUrl()
                .setId(resultSet.getInt("d_id"))
                .setUrlType(resultSet.getString("d_url_type"))
                .setComCode(getComCode(resultSet.getString("d_comcode")))
                .setUrlApp(getUrlApp(resultSet.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(resultSet.getString("d_ajax_app")))
                .setStartTime(getLocalDateTime(resultSet, "d_start_time"))
                .setEndTime(getLocalDateTime(resultSet, "d_end_time"))
                .setUrl(resultSet.getString("d_url"))
                .setAjaxUrl(resultSet.getString("d_ajax_url"))
                .setError(resultSet.getString("d_error"))
                .setAlarm(resultSet.getInt("d_alarm") > 0)
                .setEffective(resultSet.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(resultSet, "d_update_time"))
                ;
    }
}
