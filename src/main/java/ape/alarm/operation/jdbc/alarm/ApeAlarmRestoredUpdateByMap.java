package ape.alarm.operation.jdbc.alarm;

import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Map;

public class ApeAlarmRestoredUpdateByMap extends JdbcUpdateOperation implements HasEntitiesParameter<Map<String, LocalDateTime>> {

    //language=MySQL
    private final static String UPDATE_SEND_STATUS_SQL = """
            UPDATE `tb_alarm` SET `d_restored_time` = ? WHERE `d_alarm_id` = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> updateSendStatus(getDBAccess().getConnection(), getContext().getObject("map"));
    }

    public void updateSendStatus(Connection connection, Map<String, LocalDateTime> map) throws Exception {
        new PreparedStatementHelper(connection, UPDATE_SEND_STATUS_SQL)
                .addBatch(map.entrySet(), e -> new Object[]{e.getValue(), e.getKey()})
                .executeBatch()
        ;
    }
}
