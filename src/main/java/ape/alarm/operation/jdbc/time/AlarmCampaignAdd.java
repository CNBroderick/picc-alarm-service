package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmCampaign;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmCampaignAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmCampaign>, IOperationLogWriter {

    //language=MySQL
    private static final String INSERT_SQL = """
            INSERT INTO tb_camp_tm(d_comcode, d_camcode, d_start_time, d_end_time, d_alarm, d_effective, d_update_time) VALUES (?,?,?,?,?,1,NOW());
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmCampaign> alarmCampaigns = new ArrayList<>(getEntities(getContext(), "alarmCampaign"));
            add(db.getConnection(), alarmCampaigns);
            writeLogForAdd(alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
        };
    }

    public void add(Connection connection, List<AlarmCampaign> alarmCampaigns) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmCampaigns, alarmCampaign -> new Object[]{
                        alarmCampaign.getComcodeId(),
                        alarmCampaign.getName(),
                        alarmCampaign.getStartTime(),
                        alarmCampaign.getEndTime(),
                        alarmCampaign.isAlarm()
                })
                .executeInsertBatch(alarmCampaigns, (a, id) -> a.setId(id).setUpdateTime(LocalDateTime.now()));
    }
}
