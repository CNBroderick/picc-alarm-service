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

public class AlarmSlaEffectiveUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSla>, IOperationLogWriter {

    //language=MySQL
    private static final String UPDATE_SQL = """
            UPDATE tb_alarm_sla SET d_effective = ?, d_update_time = NOW() WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            Connection connection = db.getConnection();
            List<AlarmSla> alarmSlas = new ArrayList<>(getEntities(getContext(), "alarmSla"));
            String sql = "SELECT * FROM tb_alarm_send_log WHERE d_id IN (%s);".formatted(
                    alarmSlas.stream().map(AlarmSla::getId).map(String::valueOf).collect(Collectors.joining(", ")));
            OperationLog operationLog = createBeforeUpdate(alarmSlas.size(), sql);
            update(connection, alarmSlas);
            writeLogForUpdate(operationLog, sql);
        };
    }

    public void update(Connection connection, List<AlarmSla> alarmSlas) throws Exception {
        new PreparedStatementHelper(connection, UPDATE_SQL)
                .addBatch(alarmSlas, alarmSla -> new Object[]{
                        alarmSla.isEffective(),
                        alarmSla.getId(),
                })
                .executeBatch()
        ;
    }
}
