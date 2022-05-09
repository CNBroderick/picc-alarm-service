package ape.alarm.operation.jdbc.master;

import ape.alarm.operation.jdbc.mapper.ComCodeMapper;
import dataq.core.operation.JdbcQueryOperation;

public class ComCodeQuery extends JdbcQueryOperation {

    public ComCodeQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new ComCodeMapper());
    }

    @Override
    public String createSQLSelect() {
        return "SELECT * FROM tb_comcode;";
    }
}
