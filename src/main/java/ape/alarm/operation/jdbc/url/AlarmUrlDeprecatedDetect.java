package ape.alarm.operation.jdbc.url;

import ape.alarm.entity.url.AlarmUrl;
import ape.alarm.operation.jdbc.mapper.AlarmUrlRowMapper;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmUrlDeprecatedDetect extends JdbcUpdateOperation {

    //language=MySQL
    private static final String FIND_SQL = """
            SELECT *
            FROM tb_alarm_url
            WHERE d_effective = 1
              AND d_id NOT IN (
                SELECT d_id
                FROM tb_alarm_url
                WHERE d_effective = 1
                GROUP BY d_comcode, d_url_type, d_url_app, d_url, d_ajax_app, d_ajax_url, d_error
                HAVING COUNT(*) = 1
            )
            ORDER BY d_comcode, d_url_type, d_url_app, d_url, d_ajax_app, d_ajax_url, d_error, d_update_time DESC;
            """;

    //language=MySQL
    private static final String UPDATE_SQL = """
            UPDATE tb_alarm_url SET d_effective = 0 WHERE d_id = ?;
            """;

    //language=MySQL
    private static final String CHECK_PAGE_TYPE_DF_IS_NULL_SQL = """
            UPDATE tb_alarm_url SET d_ajax_app = NULL, d_ajax_url = NULL, d_error = NULL WHERE d_url_type = '页面';
            """;

    //language=MySQL
    private static final String CHECK_AJAX_TYPE_DF_IS_NULL_SQL = """
            UPDATE tb_alarm_url SET d_error = NULL WHERE d_url_type = 'AJAX';
            """;

    //language=MySQL
    private static final String CHECK_JS_ERROR_TYPE_DF_IS_NULL_SQL = """
            UPDATE tb_alarm_url SET d_ajax_app = NULL, d_ajax_url = NULL WHERE d_url_type = 'JS错误';
            """;

    private static final String[] BEFORE_EXECUTE_SQL = new String[]{
            CHECK_PAGE_TYPE_DF_IS_NULL_SQL,
            CHECK_AJAX_TYPE_DF_IS_NULL_SQL,
            CHECK_JS_ERROR_TYPE_DF_IS_NULL_SQL
    };

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> detect(db.getConnection());
    }

    public void detect(Connection connection) throws Exception {
        for (String sql : BEFORE_EXECUTE_SQL) {
            new PreparedStatementHelper(connection, sql).executeUpdate();
        }

        Map<String, List<AlarmUrl>> alarmUrls = new PreparedStatementHelper(connection, FIND_SQL)
                .executeQuery()
                .asList(new AlarmUrlRowMapper()).stream().collect(Collectors.groupingBy(AlarmUrl::computeMainFiledSha512, Collectors.mapping(
                        Function.identity(), Collectors.toList()
                )));

        List<AlarmUrl> deprecated = new ArrayList<>();
        alarmUrls.values().forEach(urls -> deprecated.addAll(AlarmUrl.computeDeprecated(urls)));
        deprecated.forEach(a -> a.setEffective(true));

        PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);
        for (AlarmUrl alarmUrl : deprecated) {
            statement.setInt(1, alarmUrl.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
        statement.close();
        LoggerFactory.getLogger(getClass()).info("处理%d条过期关键URL（共%d条）。id：%s。".formatted(
                deprecated.size(), alarmUrls.values().stream().mapToInt(List::size).sum(),
                deprecated.stream().map(a -> a.getId() + "").collect(Collectors.joining("、"))
        ));
    }
}
