package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.transmission.UrlFrontPageMapping;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class UrlFrontPageMappingRowMapper implements IEntityRowMapper<UrlFrontPageMapping> {

    public UrlFrontPageMappingRowMapper() {

    }

    @Override
    public UrlFrontPageMapping mapRow(ResultSet r) throws Exception {
        return new UrlFrontPageMapping()
                .setId(r.getString("d_id"))
                .setName(r.getString("d_name"))
                .setUrl(r.getString("d_url"))
                ;
    }

}
