package ape.alarm.operation.jdbc.sla;

import ape.master.entity.code.ComCode;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.entity.dao.IEntityRowMapper;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractSlowSlaQuery<T> extends JdbcQueryOperation implements IOperationLogWriter {

    public AbstractSlowSlaQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(getEntityRowMapper());
    }

    @Override
    public String createSQLSelect() {
        List<String> conditions = createWhereConditions();
        return "SELECT * FROM " + getTableName() + " " + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions))
               + " ORDER BY d_comcode, d_url_app, d_ajax_app, d_alarm_type, d_url, d_start_time;";
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(super.doExecute());
    }

    protected abstract String getTableName();

    protected abstract IEntityRowMapper<T> getEntityRowMapper();

    protected List<String> createWhereConditions() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("id")).ifPresent(id -> conditions.add(" `d_id` = '" + id + "'"));

        Optional.ofNullable(getContext().getObject("comcode")).ifPresent(comcode -> conditions.add(" `d_comcode` = '" + comcode + "'"));

        Optional.ofNullable(getContext().<Collection<ComCode>>getObject("userComcodeList")).ifPresent(comcodeList -> conditions.add(
                comcodeList.isEmpty() ? "`d_comcode` != `d_comcode`"
                                      : "`d_comcode` IN ('" + comcodeList.stream().map(ComCode::getId).collect(Collectors.joining("','")) + "')"
        ));

        Optional.ofNullable(getContext().getObject("ajaxApp")).ifPresent(ajaxApp -> conditions.add(" `d_ajax_app` = '" + ajaxApp + "'"));

        Optional.ofNullable(getContext().getObject("urlApp")).ifPresent(urlApp -> conditions.add(" `d_url_app` = '" + urlApp + "'"));

        Optional.ofNullable(getContext().getObject("url")).ifPresent(url -> conditions.add(" `d_url` = '" + url + "'"));

        Optional.ofNullable(getContext().getObject("alarmType")).ifPresent(alarmType -> conditions.add(" `d_alarm_type` = '" + alarmType + "'"));

        Optional.ofNullable(getContext().<Boolean>getObject("effective"))
                .ifPresent(effective -> conditions.add(" `d_alarm` = " + (effective ? 1 : 0) + ""));

        Optional.ofNullable(getContext().<Number>getObject("minAvg"))
                .ifPresent(number -> conditions.add(" `d_avg` >= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("maxAvg"))
                .ifPresent(number -> conditions.add(" `d_avg` <= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("minSla"))
                .ifPresent(number -> conditions.add(" `d_avg` >= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("maxSla"))
                .ifPresent(number -> conditions.add(" `d_avg` <= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("minQuartile1"))
                .ifPresent(number -> conditions.add(" `d_quartile1` >= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("maxQuartile1"))
                .ifPresent(number -> conditions.add(" `d_quartile1` <= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("minQuartile2"))
                .ifPresent(number -> conditions.add(" `d_quartile2` >= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("maxQuartile2"))
                .ifPresent(number -> conditions.add(" `d_quartile2` <= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("minQuartile3"))
                .ifPresent(number -> conditions.add(" `d_quartile3` >= " + number + ""));

        Optional.ofNullable(getContext().<Number>getObject("maxQuartile3"))
                .ifPresent(number -> conditions.add(" `d_quartile3` <= " + number + ""));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxUpdateTime"))
                .ifPresent(maxUpdateTime -> conditions.add(" `d_update_time` <= '" + LocalDateTimeFormatter.Short(maxUpdateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minUpdateTime"))
                .ifPresent(minUpdateTime -> conditions.add(" `d_update_time` >= '" + LocalDateTimeFormatter.Short(minUpdateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minStartTime"))
                .ifPresent(minStartTime -> conditions.add(" `d_start_time` >= '" + LocalDateTimeFormatter.Short(minStartTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxStartTime"))
                .ifPresent(maxStartTime -> conditions.add(" `d_start_time` <= '" + LocalDateTimeFormatter.Short(maxStartTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minEndTime"))
                .ifPresent(minEndTime -> conditions.add(" `d_End_time` >= '" + LocalDateTimeFormatter.Short(minEndTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxEndTime"))
                .ifPresent(maxEndTime -> conditions.add(" `d_End_time` <= '" + LocalDateTimeFormatter.Short(maxEndTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("maxUpdateTime"))
                .ifPresent(maxUpdateTime -> conditions.add(" `d_update_time` <= '" + LocalDateTimeFormatter.Short(maxUpdateTime) + "'"));

        Optional.ofNullable(getContext().<LocalDateTime>getObject("minUpdateTime"))
                .ifPresent(minUpdateTime -> conditions.add(" `d_update_time` >= '" + LocalDateTimeFormatter.Short(minUpdateTime) + "'"));

        Optional.ofNullable(getContext().<Number>getObject("year")).map(Number::intValue)
                .ifPresent(year -> conditions.add(" YEAR(`d_start_time`) = %d".formatted(year)));

        Optional.ofNullable(getContext().<Number>getObject("month")).map(Number::intValue)
                .ifPresent(month -> conditions.add(" MONTH(`d_start_time`) = %d".formatted(month)));

        return conditions;
    }
}
