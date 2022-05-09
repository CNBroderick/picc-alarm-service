package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSla;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmSlaInvalid extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSla>, IOperationLogWriter {
    static final String INVALID_SQL = "UPDATE tb_alarm_sla SET d_effective = 0 WHERE d_id = ?;";

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            Connection connection = db.getConnection();
            List<AlarmSla> alarmSlas = new ArrayList<>(getEntities(getContext(), "alarmSla"));
            String sql = "SELECT * FROM tb_alarm_sla WHERE d_id IN (%s);".formatted(
                    alarmSlas.stream().map(AlarmSla::getId).map(String::valueOf).collect(Collectors.joining(", ")));
            OperationLog operationLog = createBeforeUpdate(alarmSlas.size(), sql);
            invalid(connection, alarmSlas);
            invalid(getDBAccess().getConnection(), new ArrayList<>(getEntities(getContext(), "alarmSla")));
            writeLogForUpdate(operationLog, sql);
        };
    }

    public void invalid(Connection connection, List<AlarmSla> alarmSlas) throws Exception {
        invalid(true, connection, alarmSlas);
    }

    public void invalid(boolean commit, Connection connection, List<AlarmSla> alarmSlas) throws Exception {
        if (alarmSlas == null || alarmSlas.isEmpty()) return;
        new PreparedStatementHelper(connection, INVALID_SQL)
                .addBatch(alarmSlas, alarmSla -> new Object[]{alarmSla.getId()})
                .executeBatch(commit);
    }

}
