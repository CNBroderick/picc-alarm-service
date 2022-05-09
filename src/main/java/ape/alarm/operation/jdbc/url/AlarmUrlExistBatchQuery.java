package ape.alarm.operation.jdbc.url;

import ape.master.entity.common.log.IOperationLogWriter;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;
import org.bklab.quark.element.HasEntitiesParameter;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.util.Set;
import java.util.stream.Collectors;

public class AlarmUrlExistBatchQuery extends JdbcQueryOperation implements HasEntitiesParameter<Integer>, IOperationLogWriter {

    public AlarmUrlExistBatchQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper((IEntityRowMapper<Integer>) resultSet -> resultSet.getInt("d_id"));
    }

    @Override
    public OperationResult doExecute() throws Exception {
        return writeLogForQuery(successResult(getDBAccess()
                .queryForRecordset(createSQLSelect())
                .asList().stream().map(r -> r.getInt("d_id")).collect(Collectors.toUnmodifiableSet())));
    }

    @Override
    public String createSQLSelect() {
        Set<String> idSet = getEntities(getContext(), "id", "ids")
                .stream().map(i -> i + "").collect(Collectors.toUnmodifiableSet());
        return "SELECT d_id FROM tb_alarm_url" + (idSet.isEmpty() ? "" : " WHERE d_id IN (" + String.join(", ", idSet) + ")") + ";";
    }
}
