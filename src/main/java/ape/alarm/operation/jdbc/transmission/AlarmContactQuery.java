package ape.alarm.operation.jdbc.transmission;

import ape.alarm.operation.jdbc.mapper.AlarmContactRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class AlarmContactQuery extends JdbcQueryOperation implements IOperationLogWriter {

    public AlarmContactQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmContactRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> conditions = createWhereSelect();
        return """
                SELECT * FROM tb_alarm_contact %s ORDER BY d_comcode, d_name;
                """.formatted(conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions));
    }

    public List<String> createWhereSelect() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("id")).ifPresent(id -> conditions.add("`d_id` = '" + id + "'"));

        Optional.ofNullable(getContext().getObject("comcode")).ifPresent(comcode -> conditions.add("`d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(getContext().<Collection<String>>getObject("comcodeList")).ifPresent(comcodeList -> conditions.add(
                comcodeList.isEmpty() ? "`d_comcode` != `d_comcode`" : "`d_comcode` IN ('" + String.join("', '", comcodeList) + "')"));

        Optional.ofNullable(getContext().getObject("name")).ifPresent(name -> conditions.add("`d_name` = '" + name + "'"));

        Optional.ofNullable(getContext().getObject("account")).ifPresent(account -> conditions.add("`d_account` = '" + account + "'"));

        Optional.ofNullable(getContext().getObject("channel")).ifPresent(channel -> conditions.add("`d_channel` = '" + channel + "'"));

        Optional.ofNullable(getContext().<Boolean>getObject("effective")).ifPresent(effective ->
                conditions.add("`d_effective` = '" + (effective ? 1 : 0) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("updateTime")).ifPresent(updateTime ->
                conditions.add("`d_update_time` = '" + LocalDateTimeFormatter.Short(updateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minUpdateTime")).ifPresent(updateTime ->
                conditions.add("`d_update_time` >= '" + LocalDateTimeFormatter.Short(updateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxUpdateTime")).ifPresent(updateTime ->
                conditions.add("`d_update_time` <= '" + LocalDateTimeFormatter.Short(updateTime) + "'"));

        return conditions;
    }
}
