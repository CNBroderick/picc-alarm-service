package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.ApeAlarmDetail;
import ape.alarm.operation.jdbc.mapper.ApeAlarmDetailRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationContext;
import dataq.core.operation.OperationResult;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApeAlarmDetailQuery extends JdbcQueryOperation implements IApeAlarmCondition, IOperationLogWriter {

    public ApeAlarmDetailQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new ApeAlarmDetailRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        List<ApeAlarmDetail> details = queryDetails(getDBAccess().getConnection());
        if (details.isEmpty()) return successResult(details);

        initApeAlarm(getDBAccess().getConnection(), details);

        if (getContext().getObject("userId") != null) writeLogForQuery(details);

        return successResult(details);
    }

    private void initApeAlarm(Connection connection, Collection<ApeAlarmDetail> details) throws Exception {
        Map<Integer, List<ApeAlarmDetail>> aidSet = details.stream().filter(d -> d.getAid() > 0)
                .collect(Collectors.groupingBy(ApeAlarmDetail::getAid, Collectors.mapping(Function.identity(), Collectors.toList())));
        Map<String, List<ApeAlarmDetail>> alarmIdSet = details.stream().filter(d -> d.getAid() < 1)
                .collect(Collectors.groupingBy(ApeAlarmDetail::getAlarmId, Collectors.mapping(Function.identity(), Collectors.toList())));
        ApeAlarmQuery alarmQuery = new ApeAlarmQuery();
        alarmQuery.queryApeAlarmsById(connection, aidSet.keySet()).forEach(a ->
                aidSet.getOrDefault(a.getId(), Collections.emptyList()).forEach(d -> d.setParentApeAlarm(a))
        );
        alarmQuery.queryApeAlarmsByAlarmId(connection, alarmIdSet.keySet()).forEach(a ->
                alarmIdSet.getOrDefault(a.getAlarmId(), Collections.emptyList()).forEach(d -> d.setParentApeAlarm(a))
        );
    }

    public List<ApeAlarmDetail> queryDetailsByAid(Connection connection, Collection<Integer> alarmIds) throws Exception {
        if (alarmIds == null || alarmIds.isEmpty()) return new ArrayList<>();
        return new PreparedStatementHelper(connection, """
                SELECT * FROM `tb_alarm_detail` WHERE `d_aid` IN (%s)
                """.formatted(alarmIds.stream().map(Number::toString).collect(Collectors.joining(", ")))
        ).executeQuery().asList(new ApeAlarmDetailRowMapper());
    }

    public List<ApeAlarmDetail> queryDetailsByAlarmId(Connection connection, Collection<String> alarmIds) throws Exception {
        if (alarmIds == null || alarmIds.isEmpty()) return new ArrayList<>();
        return new PreparedStatementHelper(connection, """
                SELECT * FROM `tb_alarm_detail` WHERE `d_aid` IN ('%s')
                """.formatted(String.join("', '", alarmIds))
        ).executeQuery().asList(new ApeAlarmDetailRowMapper());
    }

    public List<ApeAlarmDetail> queryDetails(Connection connection) throws Exception {
        return new PreparedStatementHelper(connection, createSQLSelect()).executeQuery().asList(new ApeAlarmDetailRowMapper());
    }

    @Override
    public String createSQLSelect() {
        String sql = """
                SELECT * FROM tb_alarm_detail %s;
                """.formatted(createWhereCondition(getContext()));
        LoggerFactory.getLogger(getClass()).debug(sql);
        return sql;
    }

    @Override
    public List<String> getConditions(OperationContext context) {
        List<String> conditions = IApeAlarmCondition.super.getConditions(context);

        Optional.ofNullable(getContext().<LocalDate>getObject("createDate")).ifPresent(date ->
                conditions.add("DATE(`d_alarm_time`) = '" + LocalDateTimeFormatter.Short(date) + "'"));

        return conditions;
    }
}
