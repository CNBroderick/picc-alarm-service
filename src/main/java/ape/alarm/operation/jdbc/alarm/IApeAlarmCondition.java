package ape.alarm.operation.jdbc.alarm;

import ape.master.entity.alarm.sla.AlarmSlaType;
import ape.master.entity.code.AppCode;
import dataq.core.operation.OperationContext;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface IApeAlarmCondition {

    default String createWhereCondition(OperationContext context) {
        return createWhereCondition(getConditions(context));
    }

    default String createWhereCondition(List<String> conditions) {
        return conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions);
    }

    default List<String> getConditions(OperationContext context) {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(context.getObject("id")).ifPresent(id -> conditions.add(" `d_id` = '" + id + "'"));
        Optional.ofNullable(context.<Collection<Object>>getObject("ids"))
                .map(ids -> ids.isEmpty() ? "-1" : ids.stream().map(String::valueOf).collect(Collectors.joining("', '")))
                .ifPresent(ids -> conditions.add(" `d_id` IN ('" + ids + "')"));

        Optional.ofNullable(context.getObject("aid")).ifPresent(aid -> conditions.add(" `d_aid` = '" + aid + "'"));
        Optional.ofNullable(context.<Collection<Object>>getObject("aids"))
                .map(ids -> ids.isEmpty() ? "-1" : ids.stream().map(String::valueOf).collect(Collectors.joining("', '")))
                .ifPresent(aids -> conditions.add(" `d_aid` IN ('" + aids + "')"));

        Optional.ofNullable(context.getObject("minId")).ifPresent(id -> conditions.add(" `d_id` >= '" + id + "'"));
        Optional.ofNullable(context.getObject("maxId")).ifPresent(id -> conditions.add(" `d_id` <= '" + id + "'"));

        Optional.ofNullable(context.getObject("alarmId")).ifPresent(id -> conditions.add(" `d_alarm_id` = '" + id + "'"));
        Optional.ofNullable(context.getObject("likeAlarmId")).ifPresent(id -> conditions.add(" `d_alarm_id` LIKE '%" + id + "%'"));
        Optional.ofNullable(context.getObject("sameAlarmId")).ifPresent(id -> conditions.add(" `d_alarm_id` LIKE '" + id + "#%'"));

        Optional.ofNullable(context.<Collection<String>>getObject("alarmIds")).ifPresent(ids -> conditions.add(
                ids.isEmpty() ? "`d_alarm_id` != `d_alarm_id`" : "`d_alarm_id` IN ('%s')".formatted(String.join("', '", ids))));

        Optional.ofNullable(context.getObject("comcode")).ifPresent(comcode -> conditions.add(" `d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(context.<AppCode>getObject("urlApp")).ifPresent(urlApp -> conditions.add(" `d_url_app` = '" + urlApp.getId() + "'"));

        Optional.ofNullable(context.<AppCode>getObject("ajaxApp")).ifPresent(ajaxApp -> conditions.add(" `d_url_app` = '" + ajaxApp.getId() + "'"));

        Optional.ofNullable(context.getObject("alarmType"))
                .ifPresent(alarmSlaType -> conditions.add(" `d_alarm_type` = '" + alarmSlaType + "'"));

        Optional.ofNullable(context.<AlarmSlaType>getObject("alarmSlaType"))
                .ifPresent(alarmSlaType -> conditions.add(" `d_alarm_type` = '" + alarmSlaType.name() + "'"));

        Optional.ofNullable(context.getObject("url"))
                .ifPresent(url -> conditions.add(" `d_url` = '" + url + "'"));

        Optional.ofNullable(context.getObject("urlLike"))
                .ifPresent(url -> conditions.add(" `d_url` LIKE '%" + url + "%'"));

        Optional.ofNullable(context.getObject("ajaxUrl"))
                .ifPresent(ajaxUrl -> conditions.add(" `d_ajax_url` = '" + ajaxUrl + "'"));

        Optional.ofNullable(context.getObject("ajaxUrlLike"))
                .ifPresent(ajaxUrl -> conditions.add(" `d_ajax_url` LIKE '%" + ajaxUrl + "%'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("minAlarmTime"))
                .ifPresent(dateTime -> conditions.add(" `d_alarm_time` >= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxAlarmTime"))
                .ifPresent(dateTime -> conditions.add(" `d_alarm_time` <= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("minStartTime"))
                .ifPresent(minStartTime -> conditions.add(" `d_start_time` >= '" + LocalDateTimeFormatter.Short(minStartTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxStartTime"))
                .ifPresent(maxStartTime -> conditions.add(" `d_start_time` <= '" + LocalDateTimeFormatter.Short(maxStartTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("minUpdateTime"))
                .ifPresent(minUpdateTime -> conditions.add(" `d_update_time` >= '" + LocalDateTimeFormatter.Short(minUpdateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxUpdateTime"))
                .ifPresent(maxUpdateTime -> conditions.add(" `d_update_time` <= '" + LocalDateTimeFormatter.Short(maxUpdateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxEndTime"))
                .ifPresent(maxEndTime -> conditions.add(" `d_end_time` <= '" + LocalDateTimeFormatter.Short(maxEndTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("minEndTime"))
                .ifPresent(minEndTime -> conditions.add(" `d_end_time` >= '" + LocalDateTimeFormatter.Short(minEndTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("minSendTime"))
                .ifPresent(dateTime -> conditions.add(" `d_send_time` <= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        Optional.ofNullable(context.<LocalDateTime>getObject("maxSendTime"))
                .ifPresent(dateTime -> conditions.add(" `d_send_time` >= '" + LocalDateTimeFormatter.Short(dateTime) + "'"));

        return conditions;
    }

}
