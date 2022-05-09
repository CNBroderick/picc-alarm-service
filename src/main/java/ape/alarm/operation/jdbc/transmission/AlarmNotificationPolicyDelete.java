package ape.alarm.operation.jdbc.transmission;

import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class AlarmNotificationPolicyDelete extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmNotificationPolicy>, IOperationLogWriter {

    private final static String DELETE_SQL = """
            DELETE FROM tb_alarm_notification_policy WHERE d_id = ?;
            """;

    private final static String DELETE_BINDING_SQL = """
            DELETE FROM tb_alarm_contact_binding WHERE d_np_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> delete(db.getConnection(), getEntities(getContext(), "alarmNotificationPolicy"));
    }

    public void delete(Connection connection, Collection<AlarmNotificationPolicy> alarmNotificationPolicies) throws Exception {
        OperationLogPointer pointer = createDeleteLogPointer(alarmNotificationPolicies, "tb_alarm_contact_binding", "d_id", AlarmNotificationPolicy::getId);

        new PreparedStatementHelper(connection, DELETE_BINDING_SQL)
                .addBatch(alarmNotificationPolicies, policy -> new Object[]{policy.getId()})
                .executeBatch(false);

        new PreparedStatementHelper(connection, DELETE_SQL)
                .addBatch(alarmNotificationPolicies, policy -> new Object[]{
                        policy.getId()
                }).executeBatch();

        pointer.write();
    }
}
