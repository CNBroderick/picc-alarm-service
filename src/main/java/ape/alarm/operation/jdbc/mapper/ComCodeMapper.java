package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.code.ComCode;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class ComCodeMapper implements IEntityRowMapper<ComCode> {
    @Override
    public ComCode mapRow(ResultSet resultSet) throws Exception {
        return new ComCode()
                .setId(resultSet.getString("d_id"))
                .setName(resultSet.getString("d_name"))
                .setCloud(resultSet.getString("d_cloud"))
                .setWosId(resultSet.getString("d_wos_id"))
                .setRules(resultSet.getString("d_rules"))
                ;
    }
}
