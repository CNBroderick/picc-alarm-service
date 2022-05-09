package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlarmUrlAdd extends JdbcUpdateOperation implements HasEntitiesParameter<AlarmUrl>, IOperationLogWriter {

    public static final String INSERT_SQL = "INSERT INTO tb_alarm_url(d_comcode, d_url_type, d_url_app, d_ajax_app, d_start_time, d_end_time, " +
                                            "d_url, d_ajax_url, d_error, d_alarm, d_effective, d_update_time) VALUES (?,?,?,?,?,?,?,?,?,?,1,NOW());";

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> writeLogForAdd(add(getDBAccess().getConnection(), new ArrayList<>(getEntities(getContext(), "alarmUrl")))
                , "tb_alarm_url", "d_id", AlarmUrl::getId);
    }

    public List<AlarmUrl> add(Connection connection, List<AlarmUrl> alarmUrls) throws Exception {
        return add(true, connection, alarmUrls);
    }

    public List<AlarmUrl> add(boolean commit, Connection connection, List<AlarmUrl> alarmUrls) throws Exception {
        if (alarmUrls == null || alarmUrls.isEmpty()) return alarmUrls;

        PreparedStatementHelper.createGeneratedKey(connection, INSERT_SQL)
                .addBatch(alarmUrls, alarmUrl -> new Object[]{
                        alarmUrl.getComcodeId(),
                        alarmUrl.getUrlTypeValue(),
                        alarmUrl.getUrlAppId(),
                        alarmUrl.getAjaxAppId(),
                        alarmUrl.getStartTime(),
                        alarmUrl.getEndTime(),
                        alarmUrl.getUrl(),
                        alarmUrl.getAjaxUrl(),
                        alarmUrl.getErrorJson(),
                        alarmUrl.isAlarm()
                })
                .executeInsertBatch(commit, alarmUrls, (alarmUrl, id) -> alarmUrl.setId(id).setUpdateTime(LocalDateTime.now()));

        new AlarmUrlDeprecatedDetect().detect(connection);
        return alarmUrls;
    }

}
