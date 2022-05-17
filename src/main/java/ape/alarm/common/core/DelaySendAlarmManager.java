package ape.alarm.common.core;

import ape.Application;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class DelaySendAlarmManager {

    public static final String ALARM_DELAY_SEND_QUEUE_REDIS_KEY = "ALARM_DELAY_SEND_QUEUE";

    private static DelaySendAlarmManager instance;
    private final StringRedisTemplate stringRedisTemplate;

    public DelaySendAlarmManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public static DelaySendAlarmManager getInstance() {
        if (instance == null) {
            instance = Application.getApplicationContext().getBean(DelaySendAlarmManager.class);
        }
        return instance;
    }

    @Scheduled(cron = "34 12 0/1 * * ?")
    public void execute() {
        log.info("延迟告警发送服务开始运行。");
        int count = 0;
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        String data = ops.leftPop(ALARM_DELAY_SEND_QUEUE_REDIS_KEY);
        while (data != null) {
            try {
                GsonJsonObjectUtil json = new GsonJsonObjectUtil(data);

                switch (json.string("type")) {
                    case "set" -> stringRedisTemplate.opsForSet().add(json.string("key"), json.string("data"));
                    case "list" -> stringRedisTemplate.opsForList().rightPush(json.string("key"), json.string("data"));
                    default -> log.warn("延迟告警发送服务恢复失败，不支持的恢复类型：\n" + data);
                }

                data = ops.leftPop(ALARM_DELAY_SEND_QUEUE_REDIS_KEY);
            } catch (Exception e) {
                log.error("延迟告警发送服务恢复错误：\n" + data, e);
            }
        }
        log.info("延迟告警发送服务结束运行，重启" + count + "个发送任务。");
    }

    public void addToDelay(String redisKey, String type, String data) {
        JsonObject json = new JsonObject();
        json.addProperty("key", redisKey);
        json.addProperty("data", data);
        json.addProperty("type", type);
        this.addToDelay(json);
    }

    public void addToDelay(JsonObject data) {
        stringRedisTemplate.opsForList().rightPush(ALARM_DELAY_SEND_QUEUE_REDIS_KEY, data.toString());
    }

}
