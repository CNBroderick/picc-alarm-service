package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSlowSlaCampaign;
import ape.alarm.operation.jdbc.mapper.AlarmSlowSlaCampaignRowMapper;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.util.List;
import java.util.Optional;

public class AlarmSlowSlaCampaignQuery extends AbstractSlowSlaQuery<AlarmSlowSlaCampaign> {
    @Override
    protected String getTableName() {
        return "tb_camp_slow_sla";
    }

    @Override
    protected IEntityRowMapper<AlarmSlowSlaCampaign> getEntityRowMapper() {
        return new AlarmSlowSlaCampaignRowMapper();
    }

    @Override
    protected List<String> createWhereConditions() {
        List<String> conditions = super.createWhereConditions();

        Optional.ofNullable(getContext().getObject("campaignName")).ifPresent(
                campaignName -> conditions.add(" `d_camcode` = '" + campaignName + "'"));
        Optional.ofNullable(getContext().getObject("campaignNameLike")).ifPresent(
                campaignName -> conditions.add(" `d_camcode` LIKE '%" + campaignName + "%'"));

        return conditions;
    }
}
