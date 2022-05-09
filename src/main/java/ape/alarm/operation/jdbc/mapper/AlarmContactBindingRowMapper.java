package ape.alarm.operation.jdbc.mapper;

import ape.alarm.entity.transmission.AlarmContactBinding;
import org.bklab.quark.entity.dao.IEntityRowMapper;

import java.sql.ResultSet;

public class AlarmContactBindingRowMapper implements IEntityRowMapper<AlarmContactBinding> {

    public AlarmContactBindingRowMapper() {

    }

    @Override
    public AlarmContactBinding mapRow(ResultSet r) throws Exception {
        return new AlarmContactBinding()
                .setNpId(r.getInt("d_np_id"))
                .setContactId(r.getInt("d_account"))
                .setUpdateTime(getLocalDateTime(r, "d_update_time"))
                ;
    }

}
