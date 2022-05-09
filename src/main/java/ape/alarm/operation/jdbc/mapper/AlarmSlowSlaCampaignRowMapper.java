package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.sla.AlarmSlowSlaCampaign;

import java.sql.ResultSet;

public class AlarmSlowSlaCampaignRowMapper extends AbstractAlarmMapper<AlarmSlowSlaCampaign> {

    @Override
    public AlarmSlowSlaCampaign mapRow(ResultSet r) throws Exception {
        return new AlarmSlowSlaCampaign()
                .setId(r.getInt("d_id"))
                .setCampaignName(r.getString("d_camcode"))
                .setComCode(getComCode(r.getString("d_comcode")))
                .setUrlApp(getUrlApp(r.getString("d_url_app")))
                .setAjaxApp(getAjaxApp(r.getString("d_ajax_app")))
                .setStartTime(getLocalDateTime(r, "d_start_time"))
                .setEndTime(getLocalDateTime(r, "d_end_time"))
                .setAlarmType(r.getString("d_alarm_type"))
                .setAjaxUrl(r.getString("d_ajax_url"))
                .setUrl(r.getString("d_url"))
                .setSla(r.getInt("d_sla"))
                .setAvg(r.getDouble("d_avg"))
                .setQuartile1(r.getDouble("d_quartile3"))
                .setQuartile2(r.getDouble("d_quartile2"))
                .setQuartile3(r.getDouble("d_quartile1"))
                .setEffective(r.getBoolean("d_effective"))
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                ;
    }

}
