package ape.alarm.operation.jdbc.alarm;

import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.util.Optional;

public class ApeAlarmNotRestoredAlarmIdQuery extends JdbcQueryOperation {

    @Override
    public OperationResult doExecute() throws Exception {
        return successResult(new PreparedStatementHelper(getDBAccess().getConnection(), createSQLSelect())
                .executeQuery().asMap(r -> r.getString("d_alarm_id"), r -> r.getInt("d_id")));
    }

    @Override
    public String createSQLSelect() {
        return "SELECT `d_id`, `d_alarm_id` FROM `tb_alarm` WHERE `d_restored_time` IS NULL ORDER BY `d_start_time`"
               + Optional.ofNullable(getContext().<Long>getObject("limit")).map(limit -> " LIMIT " + limit).orElse("") + ";";
    }
}
