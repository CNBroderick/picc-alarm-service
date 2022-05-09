package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmCampaign;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmCampaignUpdateEffective extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmCampaign>, IOperationLogWriter {

    //language=MySQL
    private static final String UPDATE_EFFECTIVE_SQL = """
            UPDATE tb_camp_tm SET d_effective = ?, d_update_time = NOW() WHERE d_id = ?;
            """;

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmCampaign> alarmCampaigns = new ArrayList<>(getEntities(getContext(), "alarmCampaign"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
            updateEffective(db.getConnection(), alarmCampaigns);
            writeLogForUpdate(beforeUpdate, alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
        };
    }

    public void updateEffective(Connection connection, List<AlarmCampaign> alarmCampaigns) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(alarmCampaigns, alarmCampaign -> new Object[]{alarmCampaign.isEffective(), alarmCampaign.getId()})
                .executeBatch();
    }

    public void updateEffective(Connection connection, List<AlarmCampaign> alarmCampaigns, boolean effective) throws Exception {
        PreparedStatementHelper.createGeneratedKey(connection, UPDATE_EFFECTIVE_SQL)
                .addBatch(alarmCampaigns, alarmCampaign -> new Object[]{effective, alarmCampaign.getId()})
                .executeBatch();
    }
}
