package ape.alarm.operation.jdbc.transmission;

import ape.alarm.entity.transmission.AlarmContact;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmContactDelete extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmContact>, IOperationLogWriter {

    //language=MySQL
    private static final String DELETE_SQL = """
            DELETE FROM tb_alarm_contact WHERE d_id = ?;
            """;

    //language=MySQL
    private static final String DELETE_BINDING_SQL = """
            DELETE FROM tb_alarm_contact_binding WHERE d_account = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> delete(db.getConnection(), new ArrayList<>(getEntities(getContext(), "alarmContact")));
    }

    private void delete(Connection connection, List<AlarmContact> alarmContacts) throws Exception {
        OperationLog operationLog = createLogForDelete(alarmContacts, "tb_alarm_contact", "d_id", AlarmContact::getId);

        PreparedStatementHelper.createGeneratedKey(connection, DELETE_BINDING_SQL)
                .addBatch(alarmContacts, contact -> new Object[]{contact.getId()})
                .executeBatch(false);

        PreparedStatementHelper.createGeneratedKey(connection, DELETE_SQL)
                .addBatch(alarmContacts, alarmContact -> new Object[]{
                        alarmContact.getId()
                })
                .executeBatch();

        operationLog.save(connection);
    }

}
