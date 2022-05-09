package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.time.AlarmSpecialDay;

import java.sql.ResultSet;

public class AlarmSpecialDayRowMapper extends AbstractAlarmMapper<AlarmSpecialDay> {


    @Override
    public AlarmSpecialDay mapRow(ResultSet r) throws Exception {
        return new AlarmSpecialDay()
                .setId(r.getInt("d_id"))
                .setName(r.getString("d_name"))
                .setComCode(getComCode(r.getString("d_comcode")))
                .setAppCode(getUrlApp(r.getString("d_appcode")))
                .setVacation(r.getInt("d_vacation") > 0)
                .setStartTime(getLocalDateTime(r, "d_start_time"))
                .setEndTime(getLocalDateTime(r, "d_end_time"))
                .setEffective(r.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                ;
    }
}
