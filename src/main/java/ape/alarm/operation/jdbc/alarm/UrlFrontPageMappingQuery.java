package ape.alarm.operation.jdbc.alarm;

import ape.alarm.operation.jdbc.mapper.UrlFrontPageMappingRowMapper;
import dataq.core.operation.JdbcQueryOperation;

public class UrlFrontPageMappingQuery extends JdbcQueryOperation {

    public UrlFrontPageMappingQuery() {
        setQueryFor(QueryFor.ForList);
        setRowMapper(new UrlFrontPageMappingRowMapper());
    }

    @Override
    public String createSQLSelect() {
        return """
                SELECT * FROM tb_url_front_page_mapping;
                """;
    }
}
