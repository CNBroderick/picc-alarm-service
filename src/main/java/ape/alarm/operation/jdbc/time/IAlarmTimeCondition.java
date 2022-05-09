package ape.alarm.operation.jdbc.time;

import ape.alarm.entity.url.AlarmUrl;
import dataq.core.operation.OperationContext;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface IAlarmTimeCondition {

    default List<String> getConditions(OperationContext context) {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(context.getObject("id")).ifPresent(id -> conditions.add(" `d_id` = '" + id + "'"));

        Optional.ofNullable(context.getObject("excludeId")).ifPresent(id -> conditions.add(" `d_id` != '" + id + "'"));

        Optional.ofNullable(context.<Collection<Integer>>getObject("excludeIds")).ifPresent(ids ->
                conditions.add(ids.isEmpty() ? " `d_id` != `d_id`"
                                             : "`d_id` IN (" + ids.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")")
        );

        Optional.ofNullable(context.getObject("comcode")).ifPresent(comcode -> conditions.add(" `d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(context.getObject("appcode")).ifPresent(appcode -> conditions.add(" `d_appcode` = '" + appcode + "'"));

        Optional.ofNullable(context.getObject("overrideComcode")).ifPresent(comcode ->
                conditions.add(" `d_comcode` IN('" + AlarmUrl.NATIONAL_COMCODE + "', '" + comcode + "')"));

        Optional.ofNullable(context.<Boolean>getObject("effective"))
                .ifPresent(effective -> conditions.add(" `d_effective` = " + (effective ? 1 : 0) + ""));

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


        return conditions;
    }

}
