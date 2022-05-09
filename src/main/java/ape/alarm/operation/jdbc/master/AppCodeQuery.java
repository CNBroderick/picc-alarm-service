package ape.alarm.operation.jdbc.master;

import ape.alarm.operation.jdbc.mapper.AppCodeMapper;
import dataq.core.operation.JdbcQueryOperation;
import dataq.core.operation.OperationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppCodeQuery extends JdbcQueryOperation {

    public AppCodeQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new AppCodeMapper());
    }

    @Override
    public OperationResult doExecute() throws Exception {
        List<Object> list1 = super.doExecute().asList();
        return successResult(getContext().getObject("appcode") != null
                             ? list1.stream().findFirst().orElse(null) : list1);
    }

    @Override
    public String createSQLSelect() {
        List<String> conditions = new ArrayList<>();

        Optional.ofNullable(getContext().getObject("appcode"))
                .ifPresent(appcode -> conditions.add("d_appcode = '" + appcode + "'"));

        Optional.ofNullable(getContext().getObject("type"))
                .ifPresent(type -> conditions.add("d_type = '" + type + "'"));

        return "SELECT * FROM tb_appcode" + (conditions.isEmpty() ? "" : " WHERE " + String.join(" AND ", conditions)) + ";";
    }
}
