package ape.alarm.common.email.consumer;

import ape.alarm.common.email.ApeAlarmPolicyPredicate;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.code.GeneralVariable;
import ape.master.service.common.ApeServiceProvider;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AlarmNotificationPolicyManager {

    private static AlarmNotificationPolicyManager instance;
    private final Map<String, List<AlarmNotificationPolicy>> notificationMap = new LinkedHashMap<>();
    private final ApeAlarmPolicyPredicate apeAlarmPolicyPredicate = new ApeAlarmPolicyPredicate();
    private LocalDateTime lastUpdateTime = LocalDateTime.MIN;
    private int autoReloadInterval = 5;

    public static AlarmNotificationPolicyManager getInstance() {
        if (instance == null) {
            instance = new AlarmNotificationPolicyManager();
        }

        if (instance.lastUpdateTime.plusMinutes(instance.autoReloadInterval).isBefore(LocalDateTime.now())) {
            instance.reload();
        }

        return instance;
    }

    public List<AlarmNotificationPolicy> getAlarmNotificationPolicies(ApeAlarm alarm) {
        String comCodeId = alarm.getComCodeId();
        List<AlarmNotificationPolicy> policies = new ArrayList<>(notificationMap.getOrDefault(comCodeId, Collections.emptyList()));
        if (!GeneralVariable.COMCODE.equals(comCodeId))
            policies.addAll(notificationMap.getOrDefault(GeneralVariable.COMCODE, Collections.emptyList()));

        return policies.stream().filter(policy -> apeAlarmPolicyPredicate.test(policy, alarm)).collect(Collectors.toList());
    }

    private void reload() {
        reload(ApeServiceProvider.getInstance().alarmNotificationService().findAllPolicies());
    }

    private void reload(List<AlarmNotificationPolicy> alarmNotificationPolicies) {
        Map<String, List<AlarmNotificationPolicy>> map = alarmNotificationPolicies.stream().collect(
                Collectors.groupingBy(AlarmNotificationPolicy::getComcode, Collectors.mapping(Function.identity(), Collectors.toList())));
        this.notificationMap.clear();
        this.notificationMap.putAll(map);
        this.autoReloadInterval = ParameterEnum.系统参数.后台服务自动刷新间隔.getEntryValue(5);
        this.lastUpdateTime = LocalDateTime.now();
    }

}
