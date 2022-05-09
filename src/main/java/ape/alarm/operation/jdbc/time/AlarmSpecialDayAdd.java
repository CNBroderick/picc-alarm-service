package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmSpecialDay;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmSpecialDayAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSpecialDay>, IOperationLogWriter {

    //language=MySQL
    private static final String INSERT_SQL = """
            INSERT INTO `tb_alarm_special_day`(`d_name`, `d_comcode`, `d_appcode`, `d_vacation`, `d_start_time`, `d_end_time`, `d_effective`, `d_update_time`) VALUES (?,?,?,?,?,?,1,NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmSpecialDay> alarmSpecialDays = new ArrayList<>(getEntities(getContext(), "alarmSpecialDay"));
            add(db.getConnection(), alarmSpecialDays);
            writeLogForAdd(alarmSpecialDays, "tb_alarm_special_day", "d_id", AlarmSpecialDay::getId);
        };
    }

    public void add(Connection connection, List<AlarmSpecialDay> alarmSpecialDays) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmSpecialDays, specialDay -> new Object[]{
                        specialDay.getName(),
                        specialDay.getComCodeId(),
                        specialDay.getAppCodeId(),
                        specialDay.isVacation(),
                        specialDay.getStartTime(),
                        specialDay.getEndTime()
                })
                .executeInsertBatch(alarmSpecialDays, (specialDay, id) -> specialDay.setId(id).setUpdateTime(LocalDateTime.now()));
    }
}
