package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmDetail;
import ape.alarm.entity.alarm.ApeAlarmSendStatus;
import ape.alarm.operation.jdbc.mapper.ApeAlarmRowMapper;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationContext;
import dataq.core.operation.OperationResult;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ApeAlarmQuery extends JdbcQueryOperation implements IApeAlarmCondition, IOperationLogWriter {

    public ApeAlarmQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new ApeAlarmRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        Connection connection = getDBAccess().getConnection();
        List<ApeAlarm> alarms = queryApeAlarms(connection);
        if (alarms.isEmpty()) return successResult(alarms);
        initApeAlarmDetails(connection, alarms);
        if (getContext().getObject("userId") != null) writeLogForQuery(alarms);
        return successResult(alarms);
    }

    private void initApeAlarmDetails(Connection connection, List<ApeAlarm> alarms) throws Exception {
        Map<Integer, Set<ApeAlarmDetail>> map = new LinkedHashMap<>();
        ApeAlarmDetailQuery detailQuery = new ApeAlarmDetailQuery();
        detailQuery.queryDetailsByAid(connection, alarms.stream().map(ApeAlarm::getId).collect(Collectors.toSet()))
                .forEach(d -> map.computeIfAbsent(d.getAid(), a -> new LinkedHashSet<>()).add(d));
        detailQuery.queryDetailsByAlarmId(connection, alarms.stream()
                .filter(a -> !map.containsKey(a.getId())).map(ApeAlarm::getAlarmId).collect(Collectors.toSet())
        ).forEach(d -> map.computeIfAbsent(d.getAid(), a -> new LinkedHashSet<>()).add(d));
        alarms.forEach(a -> a.setAlarmDetails(Optional.ofNullable(map.get(a.getId())).orElse(new LinkedHashSet<>())));
    }

    public List<ApeAlarm> queryApeAlarms(Connection connection) throws Exception {
        return new PreparedStatementHelper(connection, createSQLSelect()).executeQuery().asList(new ApeAlarmRowMapper());
    }

    public List<ApeAlarm> queryApeAlarmsById(Connection connection, Collection<Integer> alarmAids) throws Exception {
        if (alarmAids.isEmpty()) return new ArrayList<>();
        return new PreparedStatementHelper(connection, """
                SELECT * FROM `tb_alarm` WHERE `d_id` IN (%s)
                """.formatted(
                alarmAids.stream().map(Number::toString).collect(Collectors.joining(", "))
        )).executeQuery().asList(new ApeAlarmRowMapper());
    }

    public List<ApeAlarm> queryApeAlarmsByAlarmId(Connection connection, Collection<String> alarmIds) throws Exception {
        if (alarmIds.isEmpty()) return new ArrayList<>();
        return new PreparedStatementHelper(connection, """
                SELECT * FROM `tb_alarm` WHERE `d_alarm_id` IN ('%s')
                """.formatted(String.join("', '", alarmIds)
        )).executeQuery().asList(new ApeAlarmRowMapper());
    }

    @Override
    public String createSQLSelect() {
        OperationContext context = getContext();
        List<String> conditions = getConditions(context);

        Optional.ofNullable(context.getObject("sendStatus"))
                .ifPresent(sendStatus -> conditions.add(" `d_send_status` = '" + sendStatus + "'"));

        Optional.ofNullable(context.<Boolean>getObject("waitingSendAlarms")).ifPresent(waitingSendAlarms -> {
            if (!waitingSendAlarms) return;
            conditions.add("(`d_send_status` IS NULL || `d_send_status` = '%s')".formatted(ApeAlarmSendStatus.待发送.name()));
        });

        Optional.ofNullable(context.<Boolean>getObject("errorSendAlarms")).ifPresent(waitingSendAlarms -> {
            if (!waitingSendAlarms) return;
            conditions.add("``d_send_status` = '%s'".formatted(ApeAlarmSendStatus.发送失败.name()));
        });

        Optional.ofNullable(context.<Collection<ApeAlarmSendStatus>>getObject("sendStatusList")).ifPresent(sendStatusList -> conditions.add(
                sendStatusList.isEmpty()
                ? "`d_send_status` != `d_send_status`"
                : "`d_send_status` IN ('%s')".formatted(sendStatusList.stream().map(Enum::name).collect(Collectors.joining("', '"))))
        );

        Optional.ofNullable(context.<ApeAlarmSendStatus>getObject("alarmSendStatus"))
                .ifPresent(alarmSendStatus -> conditions.add(" `d_send_status` = '" + alarmSendStatus.name() + "'"));

        Optional.ofNullable(getContext().<Collection<ApeAlarmSendStatus>>getObject("alarmSendStatusList")).ifPresent(statusList -> conditions.add(
                statusList.isEmpty() ? "`d_send_status` != `d_send_status`"
                                     : "`d_send_status` IN ('" + statusList.stream().map(Enum::name).distinct().collect(Collectors.joining("', '")) + "')"));

        Optional.ofNullable(context.<LocalDate>getObject("alarmCreateDate"))
                .ifPresent(date -> conditions.add(" DATE(`d_alarm_time`) = '" + LocalDateTimeFormatter.Short(date) + "'"));


        Optional.ofNullable(context.<LocalDateTime>getObject("minRestoredTime"))
                .ifPresent(dateTime -> conditions.add(" `d_restored_time` <= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxRestoredTime"))
                .ifPresent(dateTime -> conditions.add(" `d_restored_time` >= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(context.<Boolean>getObject("restored")).map(restored -> restored ? "NOT NULL" : "NULL")
                .ifPresent(restored -> conditions.add(" `d_restored_time` IS " + restored));

        String sql = """
                SELECT * FROM `tb_alarm` %s
                """.formatted(createWhereCondition(conditions));

        Object limit = getContext().getObject("limit");
        sql += limit == null ? ";" : " LIMIT " + limit + ";";

        return sql;
    }
}
