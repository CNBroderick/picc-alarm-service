package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.AlarmSendLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmSendLogUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSendLog> {

    //language=MySQL
    private final static String UPDATE_SQL = """
            UPDATE `tb_alarm_send_log` SET
                `d_aid` = ?,
                `d_alarm_id` = ?,
                `d_policy_id` = ?,
                `d_channel` = ?,
                `d_account` = ?,
                `d_address` = ?,
                `d_title` = ?,
                `d_send_time` = ?,
                `d_status` = ?,
                `d_message` = ?,
                `d_attachment_name` = ?,
                `d_attachment` = ?,
                `d_data` = ?,
                `d_error_message` = ?,
                `d_error_stack` = ?
            WHERE `d_id` = ?
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> update(db.getConnection(), new ArrayList<>(getEntities(getContext(), "alarmSendLog")));
    }

    private void update(Connection connection, List<AlarmSendLog> alarmSendLogs) throws Exception {
        new PreparedStatementHelper(connection, UPDATE_SQL)
                .addBatch(alarmSendLogs, alarmSendLog -> new Object[]{
                        alarmSendLog.getAid(),
                        alarmSendLog.getAlarmId(),
                        alarmSendLog.getPolicyId(),
                        alarmSendLog.getChannelName(),
                        alarmSendLog.getAccount(),
                        alarmSendLog.getAddress(),
                        alarmSendLog.getTitle(),
                        alarmSendLog.getSendTime(),
                        alarmSendLog.getStatusName(),
                        alarmSendLog.getMessage(),
                        alarmSendLog.getAttachmentName(),
                        alarmSendLog.getAttachment(),
                        alarmSendLog.getDataJson(),
                        alarmSendLog.getErrorMessage(),
                        alarmSendLog.getErrorStack(),
                        alarmSendLog.getId(),
                })
                .executeBatch();
    }
}
