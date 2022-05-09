package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmWeekDays;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmWeekDaysUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmWeekDays>, IOperationLogWriter {

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmWeekDays> alarmWeekDays = new ArrayList<>(getEntities(getContext(), "alarmWeekDays"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmWeekDays, "tb_alarm_weekdays", "d_id", AlarmWeekDays::getId);
            update(db.getConnection(), alarmWeekDays);
            writeLogForUpdate(beforeUpdate, alarmWeekDays, "tb_alarm_weekdays", "d_id", AlarmWeekDays::getId);
        };
    }

    public void update(Connection connection, List<AlarmWeekDays> alarmWeekDays) throws Exception {
        new AlarmWeekDaysEffectiveUpdate().updateEffective(connection, alarmWeekDays, false);
        new AlarmWeekDaysAdd().add(connection, alarmWeekDays);
    }
}
