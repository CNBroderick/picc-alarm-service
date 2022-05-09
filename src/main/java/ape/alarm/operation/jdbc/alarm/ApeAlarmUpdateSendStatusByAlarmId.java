package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.ApeAlarmSendStatus;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class ApeAlarmUpdateSendStatusByAlarmId extends JdbcUpdateOperation implements HasEntitiesParameter<String> {

    //language=MySQL
    private final static String UPDATE_SEND_STATUS_SQL = """
            UPDATE `tb_alarm` SET `d_send_status` = ?, `d_send_time` = NOW() WHERE `d_alarm_id` = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> updateSendStatus(
                getDBAccess().getConnection(),
                getEntities(getContext(), "alarmId"),
                getContext().getObject("apeAlarmSendStatus")
        );
    }

    public void updateSendStatus(Connection connection, Collection<String> alarms, ApeAlarmSendStatus sendStatus) throws Exception {
        if (alarms == null || alarms.isEmpty()) return;
        new PreparedStatementHelper(connection, UPDATE_SEND_STATUS_SQL)
                .addBatch(alarms, alarm -> new Object[]{sendStatus.name(), alarm})
                .executeBatch()
        ;
    }
}
