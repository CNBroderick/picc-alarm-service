package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.transmission.UrlMicroServiceMapping;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class UrlMicroServiceMappingRowMapper implements IEntityRowMapper<UrlMicroServiceMapping> {

    public UrlMicroServiceMappingRowMapper() {

    }

    @Override
    public UrlMicroServiceMapping mapRow(ResultSet r) throws Exception {
        return new UrlMicroServiceMapping()
                .setFpId(r.getString("d_fp_id"))
                .setFpName(r.getString("d_fp_name"))
                .setMsName(r.getString("d_ms_name"))
                .setMsId(r.getString("d_ms_id"))
                .setUrl(r.getString("d_url"))
                ;
    }

}
