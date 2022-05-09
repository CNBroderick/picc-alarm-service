package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.code.AppCode;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class AppCodeMapper implements IEntityRowMapper<AppCode> {

    @Override
    public AppCode mapRow(ResultSet resultSet) throws Exception {
        return new AppCode()
                .setId(resultSet.getString("d_id"))
                .setPortal(resultSet.getString("d_portal"))
                .setFront(resultSet.getString("d_front"))
                .setName(resultSet.getString("d_name"))
                .setType(resultSet.getString("d_type"))
                .setWosId(resultSet.getString("d_wos_id"))
                .setDisplayName(resultSet.getString("d_display_name"));
    }
}
