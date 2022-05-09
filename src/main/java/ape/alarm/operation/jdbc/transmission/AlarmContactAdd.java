package ape.alarm.operation.jdbc.transmission;

import ape.alarm.entity.transmission.AlarmContact;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class AlarmContactAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmContact>, IOperationLogWriter {

    private final static String INSERT_SQL = """
            INSERT INTO tb_alarm_contact(d_comcode, d_name, d_account, d_channel, d_effective, d_update_time) VALUES (?,?,?,?,?, NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> writeLogForAdd(add(db.getConnection(), getEntityList(getContext(), "alarmContact")),
                "tb_alarm_contact", "d_id", AlarmContact::getId);
    }

    private List<AlarmContact> add(Connection connection, List<AlarmContact> alarmContacts) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmContacts, alarmContact -> new Object[]{
                        alarmContact.getComCodeId(),
                        alarmContact.getName(),
                        alarmContact.getAccount(),
                        alarmContact.getChannelJson(),
                        alarmContact.isEffective() ? 1 : 0
                })
                .executeInsertBatch(alarmContacts, (a, id) -> a.setId(id).setUpdateTime(LocalDateTime.now()));
        return alarmContacts;
    }
}
