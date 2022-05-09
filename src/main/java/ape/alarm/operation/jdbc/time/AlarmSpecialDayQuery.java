package ape.alarm.operation.jdbc.time;

import ape.alarm.operation.jdbc.mapper.AlarmSpecialDayRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlarmSpecialDayQuery extends JdbcQueryOperation implements IAlarmTimeCondition, IOperationLogWriter {

    public AlarmSpecialDayQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmSpecialDayRowMapper());
        LoggerFactory.getLogger(getClass()).debug("\n" + createSQLSelect());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> whereCondition = getWhereCondition();
        return "SELECT * FROM tb_alarm_special_day"
               + (whereCondition.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereCondition))
               + " ORDER BY d_comcode, d_start_time, d_name;";
    }

    private List<String> getWhereCondition() {
        List<String> conditions = new ArrayList<>(getConditions(getContext()));

        Optional.ofNullable(getContext().<Boolean>getObject("vacation"))
                .ifPresent(vacation -> conditions.add(" `d_vacation` = " + (vacation ? 1 : 0) + ""));

        Optional.ofNullable(getContext().<String>getObject("nameLike"))
                .ifPresent(name -> conditions.add(" `d_name` LIKE '%" + name + "%'"));

        return conditions;
    }
}
