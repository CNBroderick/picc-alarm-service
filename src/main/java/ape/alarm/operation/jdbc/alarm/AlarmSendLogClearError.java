package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.AlarmSendLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class AlarmSendLogClearError extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSendLog> {

    //language=MySQL
    private final static String UPDATE_STATUS_SQL = """
            UPDATE tb_alarm_send_log SET d_error_message = NULL, d_error_stack = NULL WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> updateStatus(db.getConnection(), getEntities(getContext(), "alarmSendLog"));
    }

    private void updateStatus(Connection connection, Collection<AlarmSendLog> alarmSendLogs) throws Exception {
        new PreparedStatementHelper(connection, UPDATE_STATUS_SQL)
                .addBatch(alarmSendLogs, alarmSendLog -> new Object[]{alarmSendLog.getId(),})
                .executeBatch();
    }
}
