package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.ApeAlarm;
import ape.master.common.util.ApeAlarmIdFactory;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class AlarmNearlyExistQuery extends JdbcQueryOperation {

    @Override
    public OperationResult doExecute() throws Exception {

        ApeAlarm apeAlarm = Objects.requireNonNull(getContext().getObject("apeAlarm"), "apeAlarm is null");

        String id = ApeAlarmIdFactory.getId(apeAlarm.getAlarmId());

        LocalDateTime maxStartTime = apeAlarm.getStartTime();
        if (maxStartTime == null) return successResult(0);
        LocalDateTime minStartTime = maxStartTime.minusMinutes(10);

        //language=MySQL
        String sql = "SELECT COUNT(*) FROM `tb_alarm` WHERE `d_start_time` > ? AND `d_start_time` < ? AND `d_alarm_id` LIKE '" + id + "#%'";

        return successResult(new PreparedStatementHelper(getDBAccess().getConnection(), sql).executeQuery(minStartTime, maxStartTime).asInt());
    }

    @Override
    public String createSQLSelect() {
        return null;
    }
}
