package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmCampaign;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmCampaignUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmCampaign>, IOperationLogWriter {

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmCampaign> alarmCampaigns = new ArrayList<>(getEntities(getContext(), "alarmCampaign"));
            OperationLog beforeUpdate = createBeforeUpdate(alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
            update(db.getConnection(), alarmCampaigns);
            writeLogForUpdate(beforeUpdate, alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
        };
    }

    public void update(Connection connection, List<AlarmCampaign> alarmCampaigns) throws Exception {
        new AlarmCampaignUpdateEffective().updateEffective(connection, alarmCampaigns, false);
        new AlarmCampaignAdd().add(connection, alarmCampaigns);
    }

}
