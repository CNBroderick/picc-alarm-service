package ape.alarm.common.jdbc;

import ape.master.entity.user.HasUserId;

public class AlarmServiceUser implements HasUserId {

    public static final AlarmServiceUser instance = new AlarmServiceUser();

    public static AlarmServiceUser getInstance() {
        return instance;
    }

    @Override
    public String getUserName() {
        return "页面性能告警后台服务";
    }

    @Override
    public String getUserRealId() {
        return "10000003";
    }

    @Override
    public String getUserComcode() {
        return "AAAAAAAA";
    }
}
