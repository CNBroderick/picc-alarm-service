package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.time.AlarmCampaign;

import java.sql.ResultSet;

public class AlarmCampaignRowMapper extends AbstractAlarmMapper<AlarmCampaign> {

    @Override
    public AlarmCampaign mapRow(ResultSet r) throws Exception {
        return new AlarmCampaign()
                .setId(r.getInt("d_id"))
                .setName(r.getString("d_camcode"))
                .setComCode(getComCode(r))
                .setStartTime(getLocalDateTime(r, "d_start_time"))
                .setEndTime(getLocalDateTime(r, "d_end_time"))
                .setAlarm(r.getInt("d_alarm") > 0)
                .setEffective(r.getInt("d_effective") > 0)
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                .setStatus(r.getInt("d_status"))
                ;
    }
}
