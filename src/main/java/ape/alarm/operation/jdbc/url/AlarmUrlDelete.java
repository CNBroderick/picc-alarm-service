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

public class AlarmUrlDelete extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {

    private final static String DELETE_SQL = "DELETE FROM tb_alarm_url WHERE d_id = ?;";

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> delete(db.getConnection(), getEntities(getContext(), "alarmUrl"));
    }

    public void delete(Connection connection, Collection<AlarmUrl> alarmUrls) throws Exception {
        OperationLogPointer pointer = createDeleteLogPointer(alarmUrls, "tb_alarm_url", "d_id", AlarmUrl::getId);
        new PreparedStatementHelper(connection, DELETE_SQL)
                .addBatch(alarmUrls, alarmUrl -> new Object[]{alarmUrl.getId()})
                .executeBatch();
        pointer.write();
    }
}
