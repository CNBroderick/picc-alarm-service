package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmSpecialDay;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmSpecialDayUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSpecialDay>, IOperationLogWriter {

    //language=MySQL
    private static final String INSERT_SQL = """
            INSERT INTO tb_alarm_weekdays(d_name, d_comcode, d_week_number, d_start_time, d_end_time, d_effective, d_update_time) VALUES (?,?,?,?,?,?,NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmSpecialDay> alarmSpecialDays = new ArrayList<>(getEntities(getContext(), "alarmSpecialDay"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmSpecialDays, "tb_alarm_special_day", "d_id", AlarmSpecialDay::getId);
            update(db.getConnection(), alarmSpecialDays);
            writeLogForUpdate(beforeUpdate, alarmSpecialDays, "tb_alarm_special_day", "d_id", AlarmSpecialDay::getId);
        };
    }

    public void update(Connection connection, List<AlarmSpecialDay> alarmSpecialDays) throws Exception {
        new AlarmSpecialDayEffectiveUpdate().updateEffective(connection, alarmSpecialDays, false);
        new AlarmSpecialDayAdd().add(connection, alarmSpecialDays);
    }
}
