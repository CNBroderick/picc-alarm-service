package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.time.AlarmWeekDays;

import java.sql.ResultSet;

public class AlarmWeekDaysRowMapper extends AbstractAlarmMapper<AlarmWeekDays> {

    @Override
    public AlarmWeekDays mapRow(ResultSet r) throws Exception {
        return new AlarmWeekDays()
                .setId(r.getInt("d_id"))
                .setName(r.getString("d_name"))
                .setComCode(getComCode(r.getString("d_comcode")))
                .setAppCode(getUrlApp(r.getString("d_appcode")))
                .setWeekDays(r.getString("d_week_number"))
                .setStartTime(getLocalTime(r, "d_start_time"))
                .setEndTime(getLocalTime(r, "d_end_time"))
                .setEffective(r.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                ;
    }
}
