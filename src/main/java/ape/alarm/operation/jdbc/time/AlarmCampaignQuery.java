package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.time.AlarmCampaignStatusEnum;
import ape.alarm.operation.jdbc.mapper.AlarmCampaignRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlarmCampaignQuery extends JdbcQueryOperation implements IAlarmTimeCondition, IOperationLogWriter {

    public AlarmCampaignQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmCampaignRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> whereCondition = getConditions(getContext());
        return "SELECT * FROM tb_camp_tm"
               + (whereCondition.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereCondition))
               + " ORDER BY d_comcode, d_camcode, d_start_time;";
    }

    private List<String> createWhereConditions() {
        List<String> conditions = new ArrayList<>(getConditions(getContext()));

        Optional.ofNullable(getContext().<String>getObject("nameLike"))
                .ifPresent(name -> conditions.add(" `d_camcode` LIKE '%" + name + "%'"));

        Optional.ofNullable(getContext().<Boolean>getObject("alarm"))
                .ifPresent(alarm -> conditions.add(" `d_alarm` = " + (alarm ? 1 : 0) + ""));

        Optional.ofNullable(getContext().<AlarmCampaignStatusEnum>getObject("status"))
                .ifPresent(status -> conditions.add(" `d_status` = " + status.getCode() + ""));

        Optional.ofNullable(getContext().<Collection<AlarmCampaignStatusEnum>>getObject("statusList")).ifPresent(status ->
                conditions.add(" `d_status` " + (status.isEmpty() ? " != `d_status` " : " IN (%s)".formatted(status.stream()
                        .map(AlarmCampaignStatusEnum::getCode).map(String::valueOf).collect(Collectors.joining(", "))))));

        return conditions;
    }
}
