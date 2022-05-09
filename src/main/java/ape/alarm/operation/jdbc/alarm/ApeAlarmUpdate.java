package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.ApeAlarm;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class ApeAlarmUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<ApeAlarm> {

    //language=MySQL
    private static final String UPDATE_SQL = """
            UPDATE tb_alarm SET d_send_status = ?, d_send_time = ? WHERE d_id;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> update(getDBAccess().getConnection(), getEntities(getContext(), "apeAlarm"));
    }

    public void update(Connection connection, Collection<ApeAlarm> alarms) throws Exception {
        new PreparedStatementHelper(connection, UPDATE_SQL)
                .addBatch(alarms, apeAlarm -> new Object[]{apeAlarm.getSendStatus(), apeAlarm.getSendTime()})
                .executeBatch();
    }
}
