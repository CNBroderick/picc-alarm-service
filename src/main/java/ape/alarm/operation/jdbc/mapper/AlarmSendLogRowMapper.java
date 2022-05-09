package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.alarm.AlarmSendLog;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class AlarmSendLogRowMapper implements IEntityRowMapper<AlarmSendLog> {

    public AlarmSendLogRowMapper() {

    }

    @Override
    public AlarmSendLog mapRow(ResultSet r) throws Exception {
        return new AlarmSendLog()
                .setId(r.getInt("d_id"))
                .setAid(r.getInt("d_aid"))
                .setAlarmId(r.getString("d_alarm_id"))
                .setPolicyId(r.getInt("d_policy_id"))
                .setChannel(r.getString("d_channel"))
                .setAccount(r.getString("d_account"))
                .setAddress(r.getString("d_address"))
                .setTitle(r.getString("d_title"))
                .setCreateTime(getLocalDateTime(r, "d_create_time"))
                .setSendTime(getLocalDateTime(r, "d_send_time"))
                .setStatus(r.getString("d_status"))
                .setMessage(r.getString("d_message"))
                .setAttachmentName(r.getString("d_attachment_name"))
                .setAttachment(r.getBlob("d_attachment"))
                .setData(r.getString("d_data"))
                .setErrorMessage(r.getString("d_error_message"))
                .setErrorStack(r.getString("d_error_stack"))
                ;
    }

}
