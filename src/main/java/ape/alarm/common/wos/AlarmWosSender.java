package ape.alarm.common.wos;

import ape.Application;
import ape.alarm.common.core.IAlarmSender;
import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.common.wos.builder.AlarmWosBuilder;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@Slf4j
public class AlarmWosSender implements IAlarmSender {

    public static final String ALARM_WOS_SEND_QUEUE_REDIS_KEY = "ALARM_WOS_SEND_QUEUE";
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警工单生成后台服务, ParameterEnum.后台服务.告警工单生成后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;

    private final SetOperations<String, String> stringSetOperations;

    public AlarmWosSender() {
        this(Application.getApplicationContext().getBean(StringRedisTemplate.class));
    }

    public AlarmWosSender(StringRedisTemplate redisTemplate) {
        stringSetOperations = redisTemplate.opsForSet();
    }

    @Override
    public boolean send(List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm) {
        if (!ABILITY_UTIL.executable()) {
            log.info("告警工单生成后台服务已禁用，取消创建。");
            return true;
        }
        if (alarm.getUrlApp().getWosId() == null) {
            log.info("url app: " + alarm.getUrlAppName() + "[" + alarm.getUrlAppId() + "]未定义工单系统应用ID，取消发送。");
            return true;
        }
        GsonJsonObjectUtil json = new GsonJsonObjectUtil(new AlarmWosBuilder(alarm).build().createPostJson(a -> a.getSendTime() == null));
        log.info("添加告警工单发送队列[" + ALARM_WOS_SEND_QUEUE_REDIS_KEY + "]\n" + json.pretty());
        stringSetOperations.add(ALARM_WOS_SEND_QUEUE_REDIS_KEY, json.pretty());
        ABILITY_UTIL.writeRunTime();
        return true;
    }
}
