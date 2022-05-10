package ape.alarm.common.bmac;

import ape.Application;
import ape.alarm.common.bmac.builder.AlarmBmacBuilder;
import ape.alarm.common.bmac.core.AlarmBmacAction;
import ape.alarm.common.core.IAlarmRestoreSender;
import ape.alarm.common.core.IAlarmSender;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.po.AlarmBmac;
import ape.alarm.entity.po.AlarmBmacData;
import ape.alarm.entity.po.AlarmBmacDataExample;
import ape.alarm.entity.po.AlarmBmacExample;
import ape.alarm.service.po.AlarmBmacDataService;
import ape.alarm.service.po.AlarmBmacService;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class AlarmBmacSender implements IAlarmSender, IAlarmRestoreSender {

    public static final String ALARM_BMAC_SEND_QUEUE_REDIS_KEY = "ALARM_BMAC_SEND_QUEUE";
    public static final String ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY = "ALARM_BMAC_RESTORE_QUEUE";
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(ParameterEnum.后台服务.启用蓝鲸告警中心推送后台服务, ParameterEnum.后台服务.蓝鲸告警中心推送后台服务最后运行时间);
    public static boolean IS_RUNNING = false;

    private final SetOperations<String, String> stringSetOperations;
    private final AlarmBmacService alarmBmacService;
    private final AlarmBmacDataService alarmBmacDataService;


    public AlarmBmacSender() {
        this(Application.getApplicationContext());
    }

    public AlarmBmacSender(ApplicationContext applicationContext) {
        this.stringSetOperations = applicationContext.getBean(StringRedisTemplate.class).opsForSet();
        this.alarmBmacService = applicationContext.getBean(AlarmBmacService.class);
        this.alarmBmacDataService = applicationContext.getBean(AlarmBmacDataService.class);
    }


    public AlarmBmacSender(SetOperations<String, String> stringSetOperations, AlarmBmacService alarmBmacService, AlarmBmacDataService alarmBmacDataService) {
        this.stringSetOperations = stringSetOperations;
        this.alarmBmacService = alarmBmacService;
        this.alarmBmacDataService = alarmBmacDataService;
    }

    @Override
    public boolean send(List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm) {
        if (!ABILITY_UTIL.executable()) {
            log.info("蓝鲸告警中心推送后台服务已禁用，取消创建。");
            return true;
        }

        AlarmBmacBuilder builder = new AlarmBmacBuilder(alarm).build();

        AlarmBmac alarmBmac = builder.getAlarmBmac();
        GsonJsonObjectUtil postData = builder.getPostData();

        alarmBmacService.insert(alarmBmac);
        alarmBmacDataService.insert(alarmBmac.createData(postData.get()));

        stringSetOperations.add(ALARM_BMAC_SEND_QUEUE_REDIS_KEY, alarmBmac.id() + "");

        return true;
    }


    @Override
    public boolean restoredAlarm(int alarmId, LocalDateTime restoreTime) {
        AlarmBmacExample alarmBmacExample = new AlarmBmacExample();
        alarmBmacExample.createCriteria().andAidEqualTo(alarmId);
        List<AlarmBmac> alarmBmacList = alarmBmacService.selectByExample(alarmBmacExample);
        if (alarmBmacList.isEmpty()) {
            log.info("告警[alarm_id=%d]未推送到蓝鲸告警中心，无需推送恢复消息。");
            return true;
        }
        AlarmBmacDataExample alarmBmacDataExample = new AlarmBmacDataExample();
        alarmBmacDataExample.createCriteria().andAidEqualTo(alarmId);
        Map<Integer, AlarmBmacData> dataMap = alarmBmacDataService.selectByExample(alarmBmacDataExample)
                .stream().collect(Collectors.toMap(AlarmBmacData::id, Function.identity(), (a, b) -> b, LinkedHashMap::new));

        for (AlarmBmac bmac : alarmBmacList) {
            bmac.closeTime(restoreTime).action(AlarmBmacAction.resolved.name());
            alarmBmacService.updateByPrimaryKey(bmac);

            AlarmBmacData data = dataMap.get(bmac.id());
            if (data == null) continue;

            alarmBmacDataService.updateByPrimaryKey(data.createRestoreRequest());
            stringSetOperations.add(ALARM_BMAC_RESTORE_QUEUE_REDIS_KEY, bmac.id() + "");
        }

        return true;
    }

}
