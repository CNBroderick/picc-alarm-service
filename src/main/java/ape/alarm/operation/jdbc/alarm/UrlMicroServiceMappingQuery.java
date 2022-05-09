package ape.alarm.operation.jdbc.alarm;

import ape.alarm.operation.jdbc.mapper.UrlMicroServiceMappingRowMapper;
import dataq.core.operation.JdbcQueryOperation;

public class UrlMicroServiceMappingQuery extends JdbcQueryOperation {

    public UrlMicroServiceMappingQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new UrlMicroServiceMappingRowMapper());
    }

    @Override
    public String createSQLSelect() {
        return """
                SELECT * FROM tb_url_micro_service_mapping;
                """;
    }
}
