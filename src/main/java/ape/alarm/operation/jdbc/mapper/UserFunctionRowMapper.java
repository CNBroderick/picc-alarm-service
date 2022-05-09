package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.user.UserFunction;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class UserFunctionRowMapper implements IEntityRowMapper<UserFunction> {
    @Override
    public UserFunction mapRow(ResultSet r) throws Exception {
        return new UserFunction()
                .setId(r.getInt("d_id"))
                .setParent(r.getInt("d_parent"))
                .setName(r.getString("d_name"))
                .setCaption(r.getString("d_caption"))
                .setIcon(r.getString("d_icon"))
                .setDescription(r.getString("d_description"))
                .setRole(r.getString("d_role"))
                .setEnable(r.getInt("d_enable") > 0)
                .setMenu(r.getInt("d_menu") > 0)
                .setClassPath(r.getString("d_class"))
                .setUrl(r.getString("d_url"))
                ;
    }
}
