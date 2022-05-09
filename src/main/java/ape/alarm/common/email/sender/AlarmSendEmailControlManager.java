package ape.alarm.common.email.sender;

import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.parameter.ParameterEnum;
import ape.master.common.util.ApeAlarmIdFactory;
import ape.master.entity.alarm.transmission.AlarmNotificationBinding;
import ape.master.service.common.ApeServiceProvider;
import ape.master.service.common.RedisStringService;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.text.StringExtractor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.BiPredicate;

@Slf4j
public class AlarmSendEmailControlManager implements BiPredicate<AlarmNotificationBinding, ApeAlarm> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String SIMILAR_ALARM_SENT_EMAIL_TIMES_KEY_PREFIX = "SIMILAR-ALARM-SENT-EMAIL-TIMES-";
    public static AlarmSendEmailControlManager instance;
    private RedisStringService redisStringService;
    private RedisTemplate<String, String> redisStringTemplate;
    private boolean similarAlarmsSentContinuously;
    private int similarAlarmsMaxDailySentTimes;
    private LocalDateTime currentKeyExpiredTime = null;

    private AlarmSendEmailControlManager() {

    }

    public static AlarmSendEmailControlManager getInstance() {
        if (instance == null) instance = new AlarmSendEmailControlManager().init();

        return instance;
    }

    public AlarmSendEmailControlManager init() {
        this.redisStringService = ApeServiceProvider.getInstance().redisStringService();
        this.redisStringTemplate = redisStringService.redisStringTemplate();
        this.similarAlarmsSentContinuously = ParameterEnum.告警系统.同类告警是否连续发送.getEntryValue(false);
        this.similarAlarmsMaxDailySentTimes = ParameterEnum.告警系统.同类告警每日发送上限.getEntryValue(3);
        return this;
    }

    public RedisTemplate<String, String> getRedisStringTemplate() {
        if (redisStringService == null) {
            this.redisStringService = ApeServiceProvider.getInstance().redisStringService();
        }

        if (redisStringTemplate == null) {
            this.redisStringTemplate = Optional.ofNullable(ApeServiceProvider.getInstance()
                    .redisStringService()).map(RedisStringService::redisStringTemplate).orElse(null);
        }

        return redisStringTemplate;
    }

    public boolean hasNearlySendAlarm(ApeAlarm apeAlarm) {
        String id = ApeAlarmIdFactory.getId(apeAlarm.getAlarmId());
        if (id == null) return false;

        Integer count = new ApeOperationBuilder().add("apeAlarm", apeAlarm).executeQuery(ApeAlarmOperationEnum.AlarmNearlyExistQuery, () -> 0);
        log.info(apeAlarm.getAlarmId() + " nearly count: " + count);
        return count != null && count > 0;
    }

    public boolean canSendByTimes(String alarmId, String account) {
        return similarAlarmsMaxDailySentTimes <= 0 || getAlarmSendTimes(alarmId, account) < similarAlarmsMaxDailySentTimes;
    }

    public int getAlarmSendTimes(String alarmId, String account) {
        return getAlarmSendTimes(getRedisHashKey(alarmId, account));
    }

    private int getAlarmSendTimes(String hashName) {
        return getAlarmSendTimes0(getRedisSendTimesHashKeyName(), hashName);
    }

    private int getAlarmSendTimes0(String redisKey, String hashName) {
        return StringExtractor.parseInt(String.valueOf(getRedisStringTemplate().opsForHash().get(redisKey, hashName)));
    }

    private void setRedisValue(String redisKey, String hashName, int times) {
        getRedisStringTemplate().opsForHash().put(redisKey, hashName, getAlarmSendTimes0(redisKey, hashName) + 1 + "");

        if (currentKeyExpiredTime == null) {
            currentKeyExpiredTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            getRedisStringTemplate().expireAt(redisKey, currentKeyExpiredTime.toInstant(ZoneOffset.ofHours(8)));
        } else if (currentKeyExpiredTime.isBefore(LocalDateTime.now())) {
            getRedisStringTemplate().delete(SIMILAR_ALARM_SENT_EMAIL_TIMES_KEY_PREFIX + currentKeyExpiredTime.format(DATE_TIME_FORMATTER));
            currentKeyExpiredTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            getRedisStringTemplate().expireAt(redisKey, currentKeyExpiredTime.toInstant(ZoneOffset.ofHours(8)));
        }

    }

    private String getRedisSendTimesHashKeyName() {
        return SIMILAR_ALARM_SENT_EMAIL_TIMES_KEY_PREFIX + LocalDate.now().format(DATE_TIME_FORMATTER);
    }

    public void addSendCount(String alarmId, String account) {
        String redisKey = getRedisHashKey(alarmId, account);
        setRedisValue(getRedisSendTimesHashKeyName(), redisKey, getAlarmSendTimes(redisKey) + 1);
    }

    private String getRedisHashKey(String alarmId, String account) {
        return ApeAlarmIdFactory.getId(alarmId) + "@" + account;
    }

    public boolean testHasNearlySendAlarm(ApeAlarm alarm) {
        return similarAlarmsSentContinuously || !hasNearlySendAlarm(alarm);
    }

    public boolean testSimilarAlarmsMaxDailySentTimes(AlarmNotificationBinding alarmNotificationBinding, ApeAlarm alarm) {
        return similarAlarmsMaxDailySentTimes <= 0 || getAlarmSendTimes(alarm.getAlarmId(), alarmNotificationBinding.getAccount()) <= similarAlarmsMaxDailySentTimes;
    }

    @Override
    public boolean test(AlarmNotificationBinding alarmNotificationBinding, ApeAlarm alarm) {
        if (!testHasNearlySendAlarm(alarm)) return Boolean.FALSE;
        if (!testSimilarAlarmsMaxDailySentTimes(alarmNotificationBinding, alarm)) return Boolean.FALSE;

        return Boolean.TRUE;
    }
}
