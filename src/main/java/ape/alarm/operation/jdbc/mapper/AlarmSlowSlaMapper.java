package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.sla.AlarmSlowSla;

import java.sql.ResultSet;

public class AlarmSlowSlaMapper extends AbstractAlarmMapper<AlarmSlowSla> {

    @Override
    public AlarmSlowSla mapRow(ResultSet resultSet) throws Exception {
        return new AlarmSlowSla()
                .setId(resultSet.getInt("d_id"))
                .setComCode(getComCode(resultSet.getString("d_comcode")))
                .setUrlApp(getUrlApp(resultSet.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(resultSet.getString("d_ajax_app")))
                .setStartTime(getLocalDateTime(resultSet, "d_start_time"))
                .setEndTime(getLocalDateTime(resultSet, "d_end_time"))
                .setAlarmType(resultSet.getString("d_alarm_type"))
                .setUrl(resultSet.getString("d_url"))
                .setAjaxUrl(resultSet.getString("d_ajax_url"))
                .setSla(resultSet.getInt("d_sla"))
                .setAvg(resultSet.getDouble("d_avg"))
                .setQuartile1(resultSet.getDouble("d_quartile3"))
                .setQuartile2(resultSet.getDouble("d_quartile2"))
                .setQuartile3(resultSet.getDouble("d_quartile1"))
                .setEffective(resultSet.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(resultSet, "d_update_time"))
                ;
    }
}
