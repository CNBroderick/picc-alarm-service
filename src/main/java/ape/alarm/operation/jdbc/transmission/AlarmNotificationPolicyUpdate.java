package ape.alarm.operation.jdbc.transmission;

import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLogPointer;
import com.google.gson.JsonArray;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.Collection;

public class AlarmNotificationPolicyUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmNotificationPolicy>, IOperationLogWriter {

    private final static String UPDATE_SQL = """
            UPDATE tb_alarm_notification_policy SET d_name = ?, d_comcode = ?, d_alarm_type = ?,
             d_url_app = ?, d_ajax_app = ?, d_channel = ?, d_effective = ?, d_update_time = NOW() WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> update(db.getConnection(), getEntities(getContext(), "alarmNotificationPolicy"));
    }

    public void update(Connection connection, Collection<AlarmNotificationPolicy> alarmNotificationPolicies) throws Exception {
        OperationLogPointer pointer = createUpdateLogPointer(alarmNotificationPolicies, "tb_alarm_notification_policy", "d_id", AlarmNotificationPolicy::getId);
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_SQL)
                .addBatch(alarmNotificationPolicies, policy -> new Object[]{
                        policy.getName(),
                        policy.getComcode(),
                        policy.getAlarmTypeName(),
                        policy.getUrlAppcode(),
                        policy.getAjaxAppcode(),
                        policy.getChannels().stream().map(Enum::name).collect(JsonArray::new, JsonArray::add, JsonArray::addAll).toString(),
                        policy.isEffective(),
                        policy.getId()
                }).executeBatch();
        pointer.write();
    }
}
