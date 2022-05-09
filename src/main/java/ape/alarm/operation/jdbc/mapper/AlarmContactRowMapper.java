package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.transmission.AlarmContact;
import ape.master.entity.code.ComCode;

import java.sql.ResultSet;

public class AlarmContactRowMapper extends AbstractAlarmMapper<AlarmContact> {

    public AlarmContactRowMapper() {
    }

    @Override
    public AlarmContact mapRow(ResultSet r) throws Exception {
        return new AlarmContact()
                .setId(r.getInt("d_id"))
                .setComCode(getComCodeMap().getOrDefault(r.getString("d_comcode"), new ComCode(r.getString("d_comcode"))))
                .setName(r.getString("d_name"))
                .setAccount(r.getString("d_account"))
                .setChannel(r.getString("d_channel"))
                .setEffective(r.getBoolean("d_effective"))
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                ;
    }

}
