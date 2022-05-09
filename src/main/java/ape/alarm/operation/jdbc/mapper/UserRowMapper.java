package ape.alarm.operation.jdbc.mapper;

import ape.master.entity.code.GeneralVariable;
import ape.master.entity.user.Role;
import ape.master.entity.user.User;
import ape.master.entity.user.UserRoleEnum;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserRowMapper extends AbstractAlarmMapper<User> {
    @Override
    public User mapRow(ResultSet r) throws Exception {
        List<String> comcodeList = getJsonArrayString(r, "d_comcode");
        Role role = new Role(r.getString("r.d_name"), r.getString("r.d_caption"),
                r.getString("r.d_desc")).setId(r.getInt("r.d_id"));
        UserRoleEnum userRoleEnum = UserRoleEnum.parse(role);
        return new User()
                .setId(r.getInt("u.d_id"))
                .setAccount(r.getString("u.d_account"))
                .setPiccId(r.getString("u.d_account"))
                .setAccountId(r.getString("u.d_account_id"))
                .setName(r.getString("u.d_name"))
                .setRole(role)
                .setComCodes(comcodeList.contains(GeneralVariable.COMCODE) || userRoleEnum == UserRoleEnum.ADMIN
                             || userRoleEnum == UserRoleEnum.ECP_OPERATIONS || userRoleEnum == UserRoleEnum.HEADQUARTERS_OPERATIONS
                             ? getComCodeMap().values().stream().filter(Objects::nonNull).collect(Collectors.toList())
                             : comcodeList.stream().map(getComCodeMap()::get).filter(Objects::nonNull).collect(Collectors.toList()))
                .setAppCodes(getAppCodes().stream().filter(Objects::nonNull).collect(Collectors.toList()))
                .setEmail(r.getString("d_email"))
                .setMobile(r.getString("d_mobile"))
                .setLoginCount(r.getInt("d_login_count"))
                .setLastLoginTime(getLocalDateTime(r, "d_last_login_time"))
                .setData(r.getString("d_data"));
    }
}
