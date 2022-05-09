package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSla;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;

import java.util.ArrayList;
import java.util.List;

public class AlarmSlaUpdate extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmSla>, IOperationLogWriter {

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            List<AlarmSla> alarmSlas = new ArrayList<>(getEntities(getContext(), "alarmSla"));
            OperationLog operationLog = createBeforeUpdate(alarmSlas, "tb_alarm_sla", "d_id", AlarmSla::getId);
            new AlarmSlaInvalid().invalid(false, db.getConnection(), alarmSlas);
            new AlarmSlaAdd().add(db.getConnection(), alarmSlas);
            writeLogForUpdate(operationLog, alarmSlas, "tb_alarm_sla", "d_id", AlarmSla::getId);
        };
    }
}
