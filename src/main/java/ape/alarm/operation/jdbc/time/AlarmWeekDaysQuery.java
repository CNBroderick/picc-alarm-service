package ape.alarm.operation.jdbc.time;

import ape.alarm.operation.jdbc.mapper.AlarmWeekDaysRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;

import java.util.List;
import java.util.Optional;

public class AlarmWeekDaysQuery extends JdbcQueryOperation implements IAlarmTimeCondition, IOperationLogWriter {

    public AlarmWeekDaysQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmWeekDaysRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> whereCondition = getConditions(getContext());

        Optional.ofNullable(getContext().<String>getObject("nameLike"))
                .ifPresent(name -> whereCondition.add(" `d_name` LIKE '%" + name + "%'"));

        return "SELECT * FROM tb_alarm_weekdays"
               + (whereCondition.isEmpty() ? "" : " WHERE " + String.join(" AND ", whereCondition))
               + " ORDER BY d_comcode, d_start_time, d_name;";
    }

}
