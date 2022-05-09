package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.alarm.operation.jdbc.mapper.AlarmUrlRowMapper;
import ape.master.entity.alarm.url.AlarmUrlType;
import ape.master.entity.code.AppCode;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AlarmUrlQuery extends JdbcQueryOperation implements IOperationLogWriter {
    public AlarmUrlQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AlarmUrlRowMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    @Override
    public String createSQLSelect() {
        List<String> conditions = createWhereConditions();
        return "SELECT * FROM tb_alarm_url " + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions))
               + " ORDER BY d_comcode, d_url_type, d_url_app, d_url, d_ajax_app, d_ajax_url, d_error;";
    }

    private List<String> createWhereConditions() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("id")).ifPresent(id -> conditions.add(" `d_id` = '" + id + "'"));

        Optional.ofNullable(getContext().getObject("comcode")).ifPresent(comcode -> conditions.add(" `d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(getContext().getObject("overrideComcode")).ifPresent(comcode ->
                conditions.add(" `d_comcode` IN('" + AlarmUrl.NATIONAL_COMCODE + "', '" + comcode + "')"));

        Optional.ofNullable(getContext().<AppCode>getObject("urlApp")).ifPresent(urlApp -> conditions.add(" `d_url_app` = '" + urlApp.getId() + "'"));

        Optional.ofNullable(getContext().<AppCode>getObject("ajaxApp")).ifPresent(ajaxApp -> conditions.add(" `d_ajax_app` = '" + ajaxApp.getId() + "'"));

        Optional.ofNullable(getContext().getObject("url")).ifPresent(url -> conditions.add(" `d_url` = '" + url + "'"));

        Optional.ofNullable(getContext().getObject("ajaxUrl")).ifPresent(ajaxUrl -> conditions.add(" `d_ajax_url` = '" + ajaxUrl + "'"));

        Optional.ofNullable(getContext().getObject("likeUrl")).ifPresent(url -> conditions.add(" `d_url` like '%" + url + "%'"));

        Optional.ofNullable(getContext().getObject("likeAjaxUrl")).ifPresent(ajaxUrl -> conditions.add(" `d_ajax_url` like '%" + ajaxUrl + "%'"));

        Optional.ofNullable(getContext().<AlarmUrlType>getObject("alarmUrlType")).ifPresent(alarmUrlType -> conditions.add(" `d_url_type` = '" + alarmUrlType.name() + "'"));

        Optional.ofNullable(getContext().<String>getObject("urlType")).ifPresent(alarmUrlType -> conditions.add(" `d_url_type` = '" + alarmUrlType + "'"));

        Optional.ofNullable(getContext().<Boolean>getObject("alarm"))
                .ifPresent(alarm -> conditions.add(" `d_alarm` = " + (alarm ? 1 : 0) + ""));

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

        return conditions;
    }
}
