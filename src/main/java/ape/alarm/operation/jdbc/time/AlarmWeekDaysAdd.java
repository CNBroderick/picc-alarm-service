package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmWeekDays;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmWeekDaysAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmWeekDays>, IOperationLogWriter {

    //language=MySQL
    private static final String INSERT_SQL = """
            INSERT INTO `tb_alarm_weekdays`(`d_name`, `d_comcode`, `d_appcode`, `d_week_number`,
             `d_start_time`, `d_end_time`, `d_effective`, `d_update_time`) VALUES (?,?,?,?,?,?,?,NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmWeekDays> alarmWeekDays = new ArrayList<>(getEntities(getContext(), "alarmWeekDays"));
            add(db.getConnection(), alarmWeekDays);
            writeLogForAdd(alarmWeekDays, "tb_alarm_weekdays", "d_id", AlarmWeekDays::getId);
        };
    }

    public void add(Connection connection, List<AlarmWeekDays> alarmWeekDays) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmWeekDays, weekdays -> new Object[]{
                        weekdays.getName(),
                        weekdays.getComCodeId(),
                        weekdays.getAppCodeId(),
                        weekdays.getWeekDaysJson(),
                        weekdays.getStartTime(),
                        weekdays.getEndTime(),
                        weekdays.isEffective()
                })
                .executeInsertBatch(alarmWeekDays, (weekDays, id) -> weekDays.setId(id).setUpdateTime(LocalDateTime.now()));
    }
}
