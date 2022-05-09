package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSlowSla;
import ape.alarm.operation.jdbc.mapper.AlarmSlowSlaMapper;
import org.bklab.quark.entity.dao.IEntityRowMapper;

public class AlarmSlowSlaQuery extends AbstractSlowSlaQuery<AlarmSlowSla> {
    @Override
    protected String getTableName() {
        return "tb_alarm_slow_sla";
    }

    @Override
    protected IEntityRowMapper<AlarmSlowSla> getEntityRowMapper() {
        return new AlarmSlowSlaMapper();
    }
}
