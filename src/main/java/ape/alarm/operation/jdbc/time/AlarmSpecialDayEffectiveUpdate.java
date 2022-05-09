package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmSpecialDay;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

public class AlarmSpecialDayEffectiveUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSpecialDay>, IOperationLogWriter {
    //language=MySQL
    private static final String UPDATE_EFFECTIVE_SQL = """
            UPDATE tb_alarm_special_day SET d_effective = ? WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmSpecialDay> alarmSpecialDays = new ArrayList<>(getEntities(getContext(), "alarmSpecialDay"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmSpecialDays, "tb_alarm_special_day", "d_id", AlarmSpecialDay::getId);
            updateEffective(db.getConnection(), alarmSpecialDays);
            writeLogForUpdate(beforeUpdate, alarmSpecialDays, "tb_alarm_special_day", "d_id", AlarmSpecialDay::getId);
        };
    }

    public void updateEffective(Connection connection, Collection<AlarmSpecialDay> specialDays) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(specialDays, specialDay -> new Object[]{
                        specialDay.isEffective(),
                        specialDay.getId()
                })
                .executeBatch();
    }

    public void updateEffective(Connection connection, Collection<AlarmSpecialDay> specialDays, boolean effective) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(specialDays, specialDay -> new Object[]{
                        effective,
                        specialDay.getId()
                })
                .executeBatch();
    }
}
