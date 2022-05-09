package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmWeekDays;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

public class AlarmWeekDaysEffectiveUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmWeekDays>, IOperationLogWriter {
    //language=MySQL
    private static final String UPDATE_EFFECTIVE_SQL = """
            UPDATE tb_alarm_weekdays SET d_effective = ? WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmWeekDays> alarmWeekDays = new ArrayList<>(getEntities(getContext(), "alarmWeekDays"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmWeekDays, "tb_alarm_weekdays", "d_id", AlarmWeekDays::getId);
            updateEffective(db.getConnection(), alarmWeekDays);
            writeLogForUpdate(beforeUpdate, alarmWeekDays, "tb_alarm_weekdays", "d_id", AlarmWeekDays::getId);
        };
    }

    public void updateEffective(Connection connection, Collection<AlarmWeekDays> alarmWeekDays) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(alarmWeekDays, weekdays -> new Object[]{
                        weekdays.isEffective(),
                        weekdays.getId()
                })
                .executeBatch();
    }

    public void updateEffective(Connection connection, Collection<AlarmWeekDays> alarmWeekDays, boolean effective) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(alarmWeekDays, weekdays -> new Object[]{
                        effective,
                        weekdays.getId()
                })
                .executeBatch();
    }
}
