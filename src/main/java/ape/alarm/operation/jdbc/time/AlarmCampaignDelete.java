package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmCampaign;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class AlarmCampaignDelete extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmCampaign>, IOperationLogWriter {

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            ArrayList<AlarmCampaign> alarmCampaigns = new ArrayList<>(getEntities(getContext(), "alarmCampaign"));
            delete(db.getConnection(), alarmCampaigns);
            writeLogForAdd(alarmCampaigns, "tb_camp_tm", "d_id", AlarmCampaign::getId);
        };
    }

    public void delete(Connection connection, List<AlarmCampaign> alarmCampaigns) throws Exception {
        new AlarmCampaignUpdateEffective().updateEffective(connection, alarmCampaigns, false);
    }

}
