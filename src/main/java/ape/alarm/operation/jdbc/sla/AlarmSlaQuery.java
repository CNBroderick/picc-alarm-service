package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.url.AlarmUrl;
import ape.alarm.operation.jdbc.mapper.AlarmSlaRowMapper;
import ape.master.entity.code.AppCode;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlarmSlaQuery extends JdbcQueryOperation implements IOperationLogWriter {

    public AlarmSlaQuery() {
        setRowMapper(new AlarmSlaRowMapper());
        setQueryFor(QueryFor.ForList);
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> conditions = createConditions();
        String sql = """
                SELECT * FROM tb_alarm_sla %s ORDER BY d_alarm_type, d_url_app, d_url, d_ajax_app, d_ajax_url, d_error;
                """.formatted(conditions.isEmpty() ? "" : "WHERE " + String.join(" AND ", conditions));
        LoggerFactory.getLogger(getClass()).debug(sql);
        return sql;
    }

    @SuppressWarnings({"DuplicatedCode"})
    private List<String> createConditions() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("id")).ifPresent(id -> conditions.add(" `d_id` = '" + id + "'"));

        Optional.ofNullable(getContext().getObject("comcode")).ifPresent(comcode -> conditions.add(" `d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(getContext().getObject("overrideComcode")).ifPresent(comcode ->
                conditions.add(" `d_comcode` IN('" + AlarmUrl.NATIONAL_COMCODE + "', '" + comcode + "')"));

        Optional.ofNullable(getContext().<AppCode>getObject("urlApp")).ifPresent(urlApp -> conditions.add(" `d_url_app` = '" + urlApp.getId() + "'"));

        Optional.ofNullable(getContext().<AppCode>getObject("ajaxApp")).ifPresent(ajaxApp -> conditions.add(" `d_ajax_app` = '" + ajaxApp.getId() + "'"));

        Optional.ofNullable(getContext().getObject("url")).ifPresent(url -> conditions.add(" `d_url` = '" + url + "'"));

        Optional.ofNullable(getContext().getObject("ajaxUrl")).ifPresent(ajaxUrl -> conditions.add(" `d_ajax_url` = '" + ajaxUrl + "'"));

        Optional.ofNullable(getContext().<String>getObject("alarmSlaType")).ifPresent(alarmUrlType -> conditions.add(" `d_alarm_type` = '" + alarmUrlType + "'"));

        Optional.ofNullable(getContext().<Boolean>getObject("effective"))
                .ifPresent(deprecated -> conditions.add(" `d_effective` = " + (deprecated ? 1 : 0) + ""));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minUpdateTime"))
                .ifPresent(minUpdateTime -> conditions.add(" `d_update_time` >= '" + LocalDateTimeFormatter.Short(minUpdateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxUpdateTime"))
                .ifPresent(maxUpdateTime -> conditions.add(" `d_update_time` <= '" + LocalDateTimeFormatter.Short(maxUpdateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minStartTime"))
                .ifPresent(minStartTime -> conditions.add(" `d_start_time` >= '" + LocalDateTimeFormatter.Short(minStartTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxStartTime"))
                .ifPresent(maxStartTime -> conditions.add(" `d_start_time` <= '" + LocalDateTimeFormatter.Short(maxStartTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minEndTime"))
                .ifPresent(minEndTime -> conditions.add(" `d_end_time` >= '" + LocalDateTimeFormatter.Short(minEndTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxEndTime"))
                .ifPresent(maxEndTime -> conditions.add(" `d_end_time` <= '" + LocalDateTimeFormatter.Short(maxEndTime) + "'"));


        Optional.ofNullable(getContext().<Number>getObject("slaWeight")).ifPresent(weight -> conditions.add(" `d_sla_weight` = " + weight + ""));
        Optional.ofNullable(getContext().<Number>getObject("minSlaWeight")).ifPresent(weight -> conditions.add(" `d_sla_weight` >= " + weight + ""));
        Optional.ofNullable(getContext().<Number>getObject("maxSlaWeight")).ifPresent(weight -> conditions.add(" `d_sla_weight` <= " + weight + ""));

        Optional.ofNullable(getContext().<Number>getObject("alarmSla")).ifPresent(sla -> conditions.add(" `d_alarm_slat` = " + sla + ""));
        Optional.ofNullable(getContext().<Number>getObject("minAlarmSla")).ifPresent(sla -> conditions.add(" `d_alarm_sla` >= " + sla + ""));
        Optional.ofNullable(getContext().<Number>getObject("maxAlarmSla")).ifPresent(sla -> conditions.add(" `d_alarm_sla` <= " + sla + ""));

        return conditions;
    }
}
