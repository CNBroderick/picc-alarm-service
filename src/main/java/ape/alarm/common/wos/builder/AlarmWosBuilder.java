package ape.alarm.common.wos.builder;

import ape.Application;
import ape.alarm.common.email.AlarmTitleFactory;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.common.wos.core.AlarmWosStatus;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.po.AlarmWos;
import ape.alarm.entity.po.AlarmWosInfo;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.alarm.service.po.AlarmWosInfoService;
import ape.alarm.service.po.AlarmWosService;
import ape.master.common.parameter.ParameterEnum;
import ape.master.common.util.ApeAlarmIdFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public class AlarmWosBuilder {

    private final ApeAlarm alarm;
    private AlarmWos alarmWos;
    private Map<Integer, AlarmWosInfo> alarmWosInfoMap;
    private ApeAlarm originAlarm;

    public AlarmWosBuilder(ApeAlarm alarm) {
        this.alarm = alarm;
    }

    public AlarmWosBuilder build(Map<Integer, AlarmWosInfo> alarmWosInfoMap, AlarmWos alarmWos) {
        this.alarmWosInfoMap = alarmWosInfoMap;
        this.alarmWos = alarmWos;
        return this;
    }

    public AlarmWosBuilder build() {
        List<ApeAlarm> allAlarm = getParentAlarm(ParameterEnum.告警系统.告警恢复等待时间.getEntryValue(15),
                alarm, new LinkedHashMap<>(Map.of(alarm.getId(), alarm)));
        this.originAlarm = allAlarm.stream().min(Comparator.comparing(ApeAlarm::getAlarmTime)).orElse(alarm);
        buildAlarmWos();
        allAlarm.stream().filter(apeAlarm -> alarmWosInfoMap.containsKey(apeAlarm.getId())).forEach(apeAlarm ->
                alarmWosInfoMap.put(apeAlarm.getId(), new AlarmWosInfoBuilder(apeAlarm,
                        apeAlarm.getAlarmDetails()).build(this.alarmWos.getId()).getAlarmWosInfo())
        );
        return this;
    }

    public String createRedisKeyName() {
        return alarmWosInfoMap.size() + "-" + originAlarm.getAlarmId();
    }

    public JsonObject createPostJson() {
        return createPostJson(a -> true);
    }

    @SuppressWarnings("SpellCheckingInspection")
    public JsonObject createPostJson(Predicate<AlarmWosInfo> predicate) {
        JsonObject json = createPostJson(this.alarmWos);
        json.add("alarminfo", alarmWosInfoMap.values().stream().filter(predicate)
                .map(this::createPostJson).collect(JsonArray::new, JsonArray::add, JsonArray::addAll));
        return json;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private JsonObject createPostJson(AlarmWos wos) {
        JsonObject json = new JsonObject();
        json.addProperty("uuid", wos.getUuid());

        json.addProperty("mntorder", alarmWos.getWfNum() == null || alarmWos.getWfNum().isBlank() ? 1 : alarmWosInfoMap.size());
        json.addProperty("imsource", wos.getImSource());
        json.addProperty("issave", wos.getIsSave());
        json.addProperty("isend", wos.getIsEnd());
        return json;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private JsonObject createPostJson(AlarmWosInfo info) {
        JsonObject json = new JsonObject();

        json.addProperty("mntno", info.getAlarmId());

        JsonArray affectSystem = new JsonArray();
        JsonObject affectSystemCode = new JsonObject();
        affectSystemCode.addProperty("systemcode", info.getAffectSystem());
        affectSystem.add(affectSystemCode);
        json.add("affectsystem", affectSystem);

        JsonArray errorSystem = new JsonArray();
        JsonObject errorSystemCode = new JsonObject();
        errorSystemCode.addProperty("systemcode", info.getErrorSystem());
        errorSystem.add(errorSystemCode);
        json.add("errorsys", errorSystem);

        json.addProperty("placetime", LocalDateTimeFormatter.Short(info.getPlaceTime()));
        json.addProperty("impppronum", info.getAffectRate());
        json.addProperty("imppampnum", info.getAffectAmplitude());
        json.addProperty("title", info.getTitle());
        json.addProperty("describes", info.getDescription());
        json.addProperty("affecttype", info.getAffectType());
        json.addProperty("ipaddr", info.getIpAddress());
        if (info.getRecoveryTime() != null) {
            json.addProperty("recoverytime", LocalDateTimeFormatter.Short(info.getRecoveryTime()));
            json.addProperty("faultdurat", Duration.between(info.getPlaceTime(), info.getRecoveryTime()).toMinutes() + "");
        }
        if (info.getFaultDuration() != null) {
            json.addProperty("faultdurat", info.getFaultDuration());
        }

        JsonArray affectArea = new JsonArray();
        JsonObject affectAreaMCode = new JsonObject();
        affectAreaMCode.addProperty("areamcode", info.getAffectArea());
        affectArea.add(affectAreaMCode);
        json.add("affectarea", affectArea);

        return json;
    }

    private void buildAlarmWos() {
        AlarmWosService service = Application.getApplicationContext().getBean(AlarmWosService.class);
        this.alarmWos = service.selectFirstByUuid(this.originAlarm.getAlarmId());
        this.alarmWosInfoMap = new LinkedHashMap<>();

        if (this.alarmWos == null) {
            this.alarmWos = createAlarmWos(this.originAlarm);
            service.insertOrUpdateSelective(this.alarmWos);
        }

        AlarmWosInfo alarmWosInfo = new AlarmWosInfoBuilder(alarm, alarm.getAlarmDetails()).build(this.alarmWos.getId()).getAlarmWosInfo();
        AlarmWosInfoService wosInfoService = Application.getApplicationContext().getBean(AlarmWosInfoService.class);
        wosInfoService.insertOrUpdateSelective(alarmWosInfo.setWosId(this.alarmWos.getId()));

        List<AlarmWosInfo> alarmWosInfos = wosInfoService.selectAllByWosId(this.alarmWos.getId());
        alarmWosInfos.forEach(wosInfo -> alarmWosInfoMap.put(wosInfo.getAlarmId(), wosInfo));
    }

    private AlarmWos createAlarmWos(ApeAlarm origin) {
        return new AlarmWos()
                .setUuid(origin.getAlarmId()).setMntOrder(1)
                .setImSource(ParameterEnum.告警系统.告警工单监控系统.getEntryValue("14"))
                .setWfTitle(AlarmTitleFactory.getInstance().createAlarmEmailTitle(origin))
                .setIsSave("1")
                .setIsEnd("0")
                .setCreateTime(LocalDateTime.now())
                .setStatus(AlarmWosStatus.待创建.name());
    }

    private List<ApeAlarm> getParentAlarm(int duration, ApeAlarm origin, Map<Integer, ApeAlarm> stepMap) {
        if (origin == null) return sortByAlarmTime(stepMap);

        int size = stepMap.size();
        List<ApeAlarm> alarmList = new ApeOperationBuilder()
                .add("sameAlarmId", ApeAlarmIdFactory.getId(origin.getAlarmId()))
                .add("minAlarmTime", origin.getAlarmTime().minusMinutes(duration))
                .add("maxAlarmTime", origin.getAlarmTime())
                .executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery);
        alarmList.forEach(a -> stepMap.put(a.getId(), a));

        if (stepMap.size() == size) return sortByAlarmTime(stepMap);

        return getParentAlarm(duration, alarmList.stream().min(Comparator.comparing(ApeAlarm::getAlarmTime)).orElse(null), stepMap);
    }

    private List<ApeAlarm> sortByAlarmTime(Map<Integer, ApeAlarm> stepMap) {
        return stepMap.values().stream().sorted(Comparator.comparing(ApeAlarm::getAlarmTime)).collect(Collectors.toList());
    }

}
