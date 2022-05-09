package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.util.Collection;

public class AlarmUrlUpdateEffective extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {
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
            Collection<AlarmUrl> entities = getEntities(getContext(), "alarmUrl");
            OperationLogPointer pointer = createUpdateLogPointer(entities, "tb_alarm_url", "d_id", AlarmUrl::getId);
            new PreparedStatementHelper(db.getConnection(), UPDATE_SQL)
                    .addBatch(entities, alarmUrl -> new Object[]{
                            alarmUrl.isEffective(),
                            alarmUrl.getId()
                    })
                    .executeBatch(true);
            pointer.write();
        };
    }
}
