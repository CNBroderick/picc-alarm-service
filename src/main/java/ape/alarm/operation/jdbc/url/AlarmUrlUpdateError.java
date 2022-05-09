package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class AlarmUrlUpdateError extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {
    private final static String UPDATE_SQL = """
            UPDATE tb_alarm_url SET
                d_error = ?,
                d_update_time = NOW()
            WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> update(db.getConnection(), getEntities(getContext(), "alarmUrl"));
    }

    public void update(Connection connection, Collection<AlarmUrl> alarmUrls) throws Exception {
        OperationLogPointer pointer = createDeleteLogPointer(alarmUrls, "tb_alarm_url", "d_id", AlarmUrl::getId);
        new PreparedStatementHelper(connection, UPDATE_SQL)
                .addBatch(alarmUrls, alarmUrl -> new Object[]{alarmUrl.getErrorJson(), alarmUrl.getId()})
                .executeBatch();
        pointer.write();
    }
}
