package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmUrlInvalid extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {
    static final String INVALID_SQL = "UPDATE tb_alarm_url SET d_effective = 0 WHERE d_id = ?;";

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmUrl> alarmUrls = new ArrayList<>(getEntities(getContext(), "alarmUrl"));
            OperationLogPointer pointer = createUpdateLogPointer(alarmUrls, "tb_alarm_url", "d_id", AlarmUrl::getId);
            invalid(getDBAccess().getConnection(), alarmUrls);
            pointer.write();
        };
    }

    public void invalid(Connection connection, List<AlarmUrl> alarmUrls) throws Exception {
        invalid(true, connection, alarmUrls);
    }

    public void invalid(boolean commit, Connection connection, List<AlarmUrl> alarmUrls) throws Exception {
        if (alarmUrls == null || alarmUrls.isEmpty()) return;
        PreparedStatementHelper.createGeneratedKey(connection, INVALID_SQL)
                .addBatch(alarmUrls, alarmUrl -> new Object[]{alarmUrl.getId()})
                .executeBatch(commit);
    }

}
