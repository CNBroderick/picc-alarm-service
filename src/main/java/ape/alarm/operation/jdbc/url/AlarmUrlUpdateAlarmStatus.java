package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.alarm.operation.jdbc.mapper.AlarmUrlRowMapper;
import ape.master.entity.code.GeneralVariable;
import ape.master.entity.common.log.IOperationLogWriter;
import ape.master.entity.common.log.OperationLog;
import ape.master.operation.ApeConnectionManager;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AlarmUrlUpdateAlarmStatus extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {
    public static void main(String[] args) throws Exception {
        Connection connection = ApeConnectionManager.getInstance().getMaster();
        new AlarmUrlDelete().delete(connection, new PreparedStatementHelper(connection, "SELECT * FROM tb_alarm_url")
                .executeQuery().asList(new AlarmUrlRowMapper())
                .stream().filter(AlarmUrl::containGeneral)
                .collect(Collectors.toList()));
    }

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {
            Connection connection = db.getConnection();
            ArrayList<AlarmUrl> alarmUrls = new ArrayList<>(getEntities(getContext(), "alarmUrl"));
            if (alarmUrls.isEmpty()) return;

            OperationLog operationLog = createBeforeUpdate(alarmUrls, "tb_alarm_url", "d_id", AlarmUrl::getId);

            new AlarmUrlInvalid().invalid(connection, alarmUrls.stream().filter(a -> !a.isEffective()).collect(Collectors.toList()));

            List<AlarmUrl> list = querySameUrls(connection, alarmUrls);
            List<AlarmUrl> addItems = alarmUrls.stream().filter(a -> canAdd(a, list)).collect(Collectors.toList());
            List<AlarmUrl> invalidItems = list.stream().filter(a -> canInvalid(a, alarmUrls)).collect(Collectors.toList());
            if (!addItems.isEmpty()) new AlarmUrlAdd().add(connection, addItems);
            if (!invalidItems.isEmpty()) new AlarmUrlInvalid().invalid(connection, invalidItems);

            List<AlarmUrl> items = new ArrayList<>();
            items.addAll(addItems);
            items.addAll(invalidItems);
            writeLogForUpdate(operationLog, items, "tb_alarm_url", "d_id", AlarmUrl::getId);

            connection.commit();
        };
    }

    private boolean canAdd(AlarmUrl alarmUrl, List<AlarmUrl> duplicates) {
        String propertiesSha512 = alarmUrl.computeMainPropertiesSha512();
        return alarmUrl.isNationalComCode() || duplicates.stream().filter(AlarmUrl::isNationalComCode)
                .noneMatch(a -> propertiesSha512.equals(a.computeMainPropertiesSha512()));
    }

    private boolean canInvalid(AlarmUrl alarmUrl, List<AlarmUrl> alarmUrls) {
        String propertiesSha512 = alarmUrl.computeMainPropertiesSha512();
        return alarmUrls.stream()
                .filter(a -> propertiesSha512.equals(a.computeMainPropertiesSha512()))
                .anyMatch(a -> Objects.equals(a.getComcodeId(), alarmUrl.getComcodeId()))
                ;
    }

    private List<AlarmUrl> querySameUrls(Connection connection, List<AlarmUrl> entities) throws Exception {
        List<String> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        for (AlarmUrl entity : entities) {
            //d_comcode = ? AND d_url_type = ? AND d_url_app = ? AND d_ajax_app = ? AND d_url = ?  AND d_ajax_url = ?  AND d_error = ? AND d_effective = ?
            List<String> c = new ArrayList<>();

            if (entity.isProvinceComCode()) {
                c.add("d_comcode = ?");
                params.add(GeneralVariable.COMCODE);
            }

            c.add("d_url_type = ?");
            params.add(entity.getUrlTypeValue());

            c.add("d_url_app = ?");
            params.add(entity.getUrlAppId());

            if (!entity.hasAjaxType()) {
                c.add("d_ajax_app IS NULL");
            } else {
                c.add("d_ajax_app = ?");
                params.add(entity.getAjaxAppId());
            }

            c.add("d_url = ?");
            params.add(entity.getUrl());

            if (!entity.hasAjaxType()) {
                c.add("d_ajax_url IS NULL");
            } else {
                c.add("d_ajax_url = ?");
                params.add(entity.getAjaxUrl());
            }

            if (!entity.hasErrorType()) {
                c.add("d_error IS NULL");
            } else {
                c.add("JSON_CONTAINS(d_error, ?)");
                params.add(entity.getErrorJson());
                c.add("JSON_CONTAINS(?, d_error)");
                params.add(entity.getErrorJson());
            }

            c.add("d_alarm = ?");
            params.add(entity.isAlarm() ? "1" : "0");

            c.add("d_effective = 1");

            conditions.add("(" + String.join(" AND ", c) + ")");
        }
        String sql = "SELECT * FROM tb_alarm_url WHERE " + String.join(" OR ", conditions) + ";";
        return new PreparedStatementHelper(connection, sql).executeQuery(params.toArray()).asList(new AlarmUrlRowMapper());
    }

}
