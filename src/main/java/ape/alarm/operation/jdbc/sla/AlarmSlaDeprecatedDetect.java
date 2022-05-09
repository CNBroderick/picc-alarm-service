package ape.alarm.operation.jdbc.sla;

import ape.alarm.entity.sla.AlarmSla;
import ape.alarm.operation.jdbc.mapper.AlarmSlaRowMapper;
import dataq.core.jdbc.IDBAccessCallback;
import dataq.core.operation.JdbcUpdateOperation;
import org.bklab.quark.entity.dao.PreparedStatementHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmSlaDeprecatedDetect extends JdbcUpdateOperation {

    private static final String QUERY_SQL = "SELECT * FROM tb_alarm_sla WHERE d_effective > 0;";
    private static final String UPDATE_SQL = "UPDATE tb_alarm_sla SET d_effective = 0, d_update_time = NOW()";

    @Override
    public IDBAccessCallback createCallBack() {
        return db -> {

            Map<String, List<AlarmSla>> map = new LinkedHashMap<>();

            new PreparedStatementHelper(db.getConnection(), QUERY_SQL).executeQuery().asList(new AlarmSlaRowMapper())
                    .stream().collect(Collectors.groupingBy(AlarmSla::computeMainFiledSha512,
                    Collectors.mapping(Function.identity(), Collectors.toList()))).forEach((sha512, alarmSlas) -> {
                if (alarmSlas.size() > 1) map.put(sha512, alarmSlas);
            });
            if (map.isEmpty()) return;

            List<AlarmSla> deprecated = new ArrayList<>();
            map.forEach((sha512, alarmSlas) -> deprecated.addAll(AlarmSla.computeDeprecated(alarmSlas)));

            db.execute(UPDATE_SQL + " WHERE d_id IN (" +
                       deprecated.stream().map(a -> "" + a.getId()).collect(Collectors.joining(", "))
                       + ")");
            db.commit();
        };
    }
}
