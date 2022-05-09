package ape.alarm.operation.jdbc.transmission;

import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.common.log.IOperationLogWriter;
import com.google.gson.JsonArray;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmNotificationPolicyAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmNotificationPolicy>, IOperationLogWriter {

    private final static String INSERT_SQL = """
            INSERT INTO tb_alarm_notification_policy(d_name, d_comcode, d_alarm_type, d_url_app,
             d_ajax_app, d_channel, d_effective, d_update_time) VALUES (?,?,?,?,?,?, 1, NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> writeLogForAdd(add(db.getConnection(), new ArrayList<>(getEntities(getContext(), "alarmNotificationPolicy"))),
                "tb_alarm_notification_policy", "d_id", AlarmNotificationPolicy::getId);
    }

    public List<AlarmNotificationPolicy> add(Connection connection, List<AlarmNotificationPolicy> alarmNotificationPolicies) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmNotificationPolicies, policy -> new Object[]{
                        policy.getName(),
                        policy.getComcode(),
                        policy.getAlarmTypeName(),
                        policy.getUrlAppcode(),
                        policy.getAjaxAppcode(),
                        policy.getChannels().stream().map(Enum::name).collect(JsonArray::new, JsonArray::add, JsonArray::addAll).toString()
                }).executeInsertBatch(alarmNotificationPolicies, (policy, id) -> policy.setId(id).setUpdateTime(LocalDateTime.now()));
        return alarmNotificationPolicies;
    }
}
