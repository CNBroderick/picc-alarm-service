package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.transmission.AlarmContactChannelTypeEnum;
import ape.alarm.operation.jdbc.mapper.AlarmSendLogRowMapper;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.entity.alarm.transmission.AlarmNotificationBinding;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.service.common.ApeServiceProvider;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmSendLogQuery extends JdbcQueryOperation implements IOperationLogWriter {

    public AlarmSendLogQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmSendLogRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        OperationResult operationResult = super.doExecute();
        List<AlarmSendLog> alarmSendLogs = operationResult.asList();
        if (alarmSendLogs == null || alarmSendLogs.isEmpty()) return operationResult;

        initApeAlarm(getDBAccess().getConnection(), alarmSendLogs);
        initPolicy(getDBAccess().getConnection(), alarmSendLogs);

        if (getContext().getObject("userId") != null) writeLogForQuery(alarmSendLogs);

        return successResult(alarmSendLogs);
    }

    private void initApeAlarm(Connection connection, Collection<AlarmSendLog> sendLogs) throws Exception {
        Map<Integer, List<AlarmSendLog>> aidSet = sendLogs.stream().filter(d -> d.getAid() > 0)
                .collect(Collectors.groupingBy(AlarmSendLog::getAid, Collectors.mapping(Function.identity(), Collectors.toList())));
        Map<String, List<AlarmSendLog>> alarmIdSet = sendLogs.stream().filter(d -> d.getAid() < 1)
                .collect(Collectors.groupingBy(AlarmSendLog::getAlarmId, Collectors.mapping(Function.identity(), Collectors.toList())));

        ApeAlarmQuery alarmQuery = new ApeAlarmQuery();
        alarmQuery.queryApeAlarmsById(connection, aidSet.keySet()).forEach(a ->
                aidSet.getOrDefault(a.getId(), Collections.emptyList()).forEach(d -> d.setAlarm(a))
        );
        alarmQuery.queryApeAlarmsByAlarmId(connection, alarmIdSet.keySet()).forEach(a ->
                alarmIdSet.getOrDefault(a.getAlarmId(), Collections.emptyList()).forEach(d -> d.setAlarm(a))
        );

        ApeAlarmDetailQuery detailQuery = new ApeAlarmDetailQuery();
        detailQuery.queryDetailsByAid(connection, aidSet.keySet())
                .forEach(d -> aidSet.getOrDefault(d.getAid(), Collections.emptyList())
                        .forEach(s -> Optional.ofNullable(s.getAlarm()).ifPresent(a -> a.getAlarmDetails().add(d))));
        detailQuery.queryDetailsByAlarmId(connection, alarmIdSet.keySet())
                .forEach(d -> aidSet.getOrDefault(d.getAid(), Collections.emptyList())
                        .forEach(s -> Optional.ofNullable(s.getAlarm()).ifPresent(a -> a.getAlarmDetails().add(d))));
    }

    private void initPolicy(Connection connection, Collection<AlarmSendLog> alarmSendLogs) {
        Set<Integer> policyIds = alarmSendLogs.stream().map(AlarmSendLog::getPolicyId).collect(Collectors.toSet());
        Map<Integer, AlarmNotificationPolicy> policyMap = ApeServiceProvider.getInstance().alarmNotificationService().findAllPolicies(policyIds)
                .stream().collect(Collectors.toMap(AlarmNotificationPolicy::getId, Function.identity(), (a, b) -> b, LinkedHashMap::new));

        Map<Integer, Map<String, AlarmNotificationBinding>> bindingMap = new LinkedHashMap<>();
        policyMap.forEach((id, policy) -> {
            Map<String, AlarmNotificationBinding> map = bindingMap.computeIfAbsent(id, i -> new LinkedHashMap<>());
            policy.getNotificationBindings().forEach(binding -> map.put(binding.getAccount(), binding));
        });

        alarmSendLogs.forEach(alarmSendLog -> alarmSendLog
                .setPolicy(policyMap.get(alarmSendLog.getPolicyId()))
                .setBinding(Optional.ofNullable(bindingMap.get(alarmSendLog.getPolicyId())).map(a -> a.get(alarmSendLog.getAccount())).orElse(null))
        );

    }

    @Override
    public String createSQLSelect() {
        String sql = """
                SELECT * FROM `tb_alarm_send_log` %s ORDER BY `d_id` DESC
                """.formatted(createWhereCondition());
        return sql + Optional.ofNullable(getContext().getObject("limit")).map(a -> " LIMIT " + a).orElse("") + ';';
    }


    private String createWhereCondition() {
        List<String> conditions = getConditions();
        return conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
    }

    private List<String> getConditions() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("id")).ifPresent(id -> conditions.add("`d_id` = " + id));
        Optional.ofNullable(getContext().getObject("minId")).ifPresent(id -> conditions.add("`d_id` >= " + id));
        Optional.ofNullable(getContext().getObject("maxId")).ifPresent(id -> conditions.add("`d_id` <= " + id));

        Optional.ofNullable(getContext().getObject("aid")).ifPresent(id -> conditions.add("`d_aid` = " + id));
        Optional.ofNullable(getContext().getObject("minAid")).ifPresent(id -> conditions.add("`d_aid` >= " + id));
        Optional.ofNullable(getContext().getObject("maxAid")).ifPresent(id -> conditions.add("`d_aid` <= " + id));

        Optional.ofNullable(getContext().<Set<Integer>>getObject("ids")).ifPresent(ids ->
                conditions.add("`d_id` " + (ids.isEmpty() ? " != `d_id`" : " IN (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")")));

        Optional.ofNullable(getContext().<Set<Integer>>getObject("aids")).ifPresent(ids ->
                conditions.add("`d_aid` " + (ids.isEmpty() ? " != `d_aid`" : " IN (" + ids.stream()
                        .map(String::valueOf).collect(Collectors.joining(",")) + ")")));

        Optional.ofNullable(getContext().getObject("comcode")).ifPresent(comcode -> conditions.add(
                "'" + comcode + "' = (SELECT d_comcode FROM tb_alarm WHERE tb_alarm_send_log.d_aid = tb_alarm.d_id)"));

        Optional.ofNullable(getContext().getObject("alarmId")).ifPresent(id -> conditions.add("`d_alarm_id` = '" + id + "'"));

        Optional.ofNullable(getContext().<Set<Object>>getObject("alarmIds")).ifPresent(ids ->
                conditions.add("`d_alarm_id` " + (ids.isEmpty() ? " != `d_alarm_id`" :
                                                  " IN ('" + ids.stream().map(String::valueOf).collect(Collectors.joining("', '")) + "')")));


        Optional.ofNullable(getContext().getObject("policyId")).ifPresent(id -> conditions.add("`d_policy_id` = " + id));

        Optional.ofNullable(getContext().<Set<Integer>>getObject("policyIds")).ifPresent(ids ->
                conditions.add("`d_policy_id` " + (ids.isEmpty() ? " != `d_policy_id`" : " IN (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")")));


        Optional.ofNullable(getContext().getObject("contactId")).ifPresent(id -> conditions.add("`d_account` = " + id));

        Optional.ofNullable(getContext().<Set<Integer>>getObject("contactIds")).ifPresent(ids ->
                conditions.add("`d_account` " + (ids.isEmpty() ? " != `d_account`" : " IN (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")")));

        Optional.ofNullable(getContext().getObject("channel")).ifPresent(channel -> conditions.add("`d_channel` = '" + channel + "'"));

        Optional.ofNullable(getContext().<AlarmContactChannelTypeEnum>getObject("alarmChannel"))
                .ifPresent(channel -> conditions.add("`d_channel` " + "= '" + channel.name() + "'"));

        Optional.ofNullable(getContext().getObject("address")).ifPresent(address -> conditions.add("`d_address` = '" + address + "'"));

        Optional.ofNullable(getContext().getObject("title")).ifPresent(title -> conditions.add("`d_title` = '" + title + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("createTime")).ifPresent(dateTime ->
                conditions.add("`d_create_time` = '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDate>getObject("createDate")).ifPresent(date ->
                conditions.add("`d_create_time` BETWEEN '" + LocalDateTimeFormatter.Short(LocalDateTime.of(date, LocalTime.MIN)) + "' AND '" + LocalDateTimeFormatter.Short(LocalDateTime.of(date, LocalTime.MAX)) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minCreateTime")).ifPresent(dateTime ->
                conditions.add("`d_create_time` >= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxCreateTime")).ifPresent(dateTime ->
                conditions.add("`d_create_time` <= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("sendTime")).ifPresent(dateTime ->
                conditions.add("`d_send_time` = '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minSendTime")).ifPresent(dateTime ->
                conditions.add("`d_send_time` >= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxSendTime")).ifPresent(dateTime ->
                conditions.add("`d_send_time` <= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(getContext().getObject("status")).ifPresent(status -> conditions.add("`d_status` = '" + status + "'"));

        Optional.ofNullable(getContext().<AlarmSendStatus>getObject("alarmSendStatus"))
                .ifPresent(status -> conditions.add("`d_status` = '" + status.name() + "'"));

        Optional.ofNullable(getContext().<Collection<AlarmSendStatus>>getObject("alarmSendStatusList")).ifPresent(statusList -> conditions.add(
                statusList.isEmpty() ? "`d_status` != `d_status`"
                                     : "`d_status` IN ('" + statusList.stream().map(Enum::name).distinct().collect(Collectors.joining("', '")) + "')"));

        return conditions;
    }
}
