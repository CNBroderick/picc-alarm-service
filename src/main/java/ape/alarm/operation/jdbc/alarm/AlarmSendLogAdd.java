package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.AlarmSendLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmSendLogAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSendLog> {

    //language=MySQL
    private final static String INSERT_SQL = """
            INSERT IGNORE INTO `tb_alarm_send_log`(`d_aid`, `d_alarm_id`, `d_policy_id`, `d_channel`, `d_account`, `d_address`, `d_title`, `d_create_time`, `d_send_time`, `d_status`,
             `d_message`, `d_attachment_name`, `d_attachment`, `d_data`, `d_error_message`, `d_error_stack`) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?);
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> add(db.getConnection(), new ArrayList<>(getEntities(getContext(), "alarmSendLog")));
    }

    private void add(Connection connection, List<AlarmSendLog> alarmSendLogs) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
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
                        alarmSendLog.getErrorStack()
                })
                .executeInsertBatch(alarmSendLogs, (alarmSendLog, id) -> alarmSendLog.setId(id).setCreateTime(LocalDateTime.now()));
        LoggerFactory.getLogger(getClass()).info("插入{}条告警发送日志。", alarmSendLogs.size());
    }
}
