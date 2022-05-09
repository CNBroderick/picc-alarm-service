package ape.alarm.common.jdbc;

import ape.master.entity.common.ApeApplicationEnum;
import ape.master.entity.user.HasUserId;
import ape.master.ui.common.AbstractApeOperationBuilder;
import org.bklab.quark.util.map.ParameterMap;

import java.util.Map;

public class ApeOperationBuilder extends AbstractApeOperationBuilder<ApeOperationBuilder> {

    public ApeOperationBuilder() {
    }

    public ApeOperationBuilder(Map<String, Object> map) {
        super(map);
    }

    public ApeOperationBuilder(ParameterMap map) {
        super(map);
    }

    @Override
    public HasUserId getHasUserId() {
        return AlarmServiceUser.getInstance();
    }

    @Override
    public ApeApplicationEnum getApplicationName() {
        return ApeApplicationEnum.告警后台服务;
    }

}
