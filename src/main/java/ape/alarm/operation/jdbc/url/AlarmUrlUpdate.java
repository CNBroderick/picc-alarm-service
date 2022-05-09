package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.element.HasPreparedStatement;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmUrlUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, HasPreparedStatement, IOperationLogWriter {
    //language=MySQL
    private final static String UPDATE_SQL = """
            UPDATE tb_alarm_url SET
                d_effective = ?,
                d_update_time = NOW()
            WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            List<AlarmUrl> entities = new ArrayList<>(getEntities(getContext(), "alarmUrl"));
            OperationLog operationLog = createBeforeUpdate(entities, "tb_alarm_url", "d_id", AlarmUrl::getId);
            update(db.getConnection(), entities);
            entities = new AlarmUrlAdd().add(db.getConnection(), entities);
            writeLogForUpdate(operationLog, entities, "tb_alarm_url", "d_id", AlarmUrl::getId);
        };
    }

    protected void update(Connection connection, List<AlarmUrl> entities) throws Exception {
        PreparedStatementHelper statement = new PreparedStatementHelper(connection, UPDATE_SQL);
        for (AlarmUrl entity : entities) {
            statement.addBatch(entity.isEffective(), entity.getId());
        }
        statement.executeBatch();
    }
}
