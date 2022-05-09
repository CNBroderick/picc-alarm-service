package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.sla.AlarmSla;

import java.sql.ResultSet;

public class AlarmSlaRowMapper extends AbstractAlarmMapper<AlarmSla> {

    @Override
    public AlarmSla mapRow(ResultSet resultSet) throws Exception {
        return new AlarmSla()
                .setId(resultSet.getInt("d_id"))
                .setComCode(getComCode(resultSet.getString("d_comcode")))
                .setUrlApp(getUrlApp(resultSet.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(resultSet.getString("d_ajax_app")))
                .setStartTime(getLocalDateTime(resultSet, "d_start_time"))
                .setEndTime(getLocalDateTime(resultSet, "d_end_time"))
                .setAlarmType(resultSet.getString("d_alarm_type"))
                .setUrl(resultSet.getString("d_url"))
                .setAjaxUrl(resultSet.getString("d_ajax_url"))
                .setAlarmSla(resultSet.getDouble("d_alarm_sla"))
                .setAlarmNum(resultSet.getInt("d_alarm_num"))
                .setError(resultSet.getString("d_error"))
                .setSlaWeight(resultSet.getInt("d_sla_weight"))
                .setEffective(resultSet.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(resultSet, "d_update_time"))
                ;
    }
}
