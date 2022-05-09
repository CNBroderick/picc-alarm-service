package ape.alarm.operation.jdbc.transmission;

import ape.alarm.entity.transmission.AlarmContact;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmContactUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmContact>, IOperationLogWriter {

    //language=MySQL
    private static final String UPDATE_SQL = """
            UPDATE tb_alarm_contact SET d_comcode = ?, d_name = ?, d_account = ?, d_channel = ?, d_effective = ?, d_update_time = NOW() WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> update(db.getConnection(), new ArrayList<>(getEntities(getContext(), "alarmContact")));
    }

    private void update(Connection connection, List<AlarmContact> alarmContacts) throws Exception {
        OperationLogPointer pointer = createUpdateLogPointer(alarmContacts, "tb_alarm_contact", "d_id", AlarmContact::getId);
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_SQL)
                .addBatch(alarmContacts, alarmContact -> new Object[]{
                        alarmContact.getComCodeId(),
                        alarmContact.getName(),
                        alarmContact.getAccount(),
                        alarmContact.getChannelJson(),
                        alarmContact.isEffective() ? 1 : 0,
                        alarmContact.getId()
                })
                .executeBatch();
        pointer.write();
    }

}
