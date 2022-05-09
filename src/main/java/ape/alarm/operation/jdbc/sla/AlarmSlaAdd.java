package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSla;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

public class AlarmSlaAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSla>, IOperationLogWriter {

    //language=MySQL
    private final static String INSERT_SQL = """
            INSERT INTO tb_alarm_sla(d_comcode, d_url_app, d_ajax_app, d_start_time, d_end_time, d_alarm_type, d_url, d_ajax_url,
             d_alarm_sla, `d_alarm_num`, d_error, d_sla_weight, d_effective, d_update_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?, 1, NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            Collection<AlarmSla> alarmSlas = getEntities(getContext(), "alarmSla");
            add(db.getConnection(), alarmSlas);
            writeLogForAdd(alarmSlas, "tb_alarm_sla", "d_id", AlarmSla::getId);
        };
    }

    public void add(Connection connection, Collection<AlarmSla> alarmSlas) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmSlas, alarmSla -> new Object[]{
                        alarmSla.getComcodeId(),
                        alarmSla.getUrlAppId(),
                        alarmSla.getAjaxAppId(),
                        alarmSla.getStartTime(),
                        alarmSla.getEndTime(),
                        alarmSla.getAlarmType(),
                        alarmSla.getUrl(),
                        alarmSla.getAjaxUrl(),
                        alarmSla.getAlarmSla(),
                        alarmSla.getAlarmNum(),
                        alarmSla.getErrorJson(),
                        alarmSla.getSlaWeight()
                })
                .executeInsertBatch(new ArrayList<>(alarmSlas), AlarmSla::setId);
    }
}
