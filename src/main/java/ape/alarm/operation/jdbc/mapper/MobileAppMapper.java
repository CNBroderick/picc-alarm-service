package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.code.MobileApp;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class MobileAppMapper implements IEntityRowMapper<MobileApp> {

    @Override
    public MobileApp mapRow(ResultSet resultSet) throws Exception {
        return new MobileApp()
                .setId(resultSet.getString("d_id"))
                .setName(resultSet.getString("d_name"))
                ;
    }
}
