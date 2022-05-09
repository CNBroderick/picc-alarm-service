package ape.alarm.entity.alarm;

import ape.alarm.entity.common.HasAjaxApp;
import ape.alarm.entity.common.HasComCode;
import ape.alarm.entity.common.HasUrlApp;
import ape.master.entity.alarm.sla.AlarmSlaType;
import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
public class ApeAlarmDetail implements HasComCode<ApeAlarmDetail>, HasAjaxApp<ApeAlarmDetail>, HasUrlApp<ApeAlarmDetail> {

    /**
     * 告警明细唯一id
     */
    private int id;
    /**
     * 告警表 d_id
     */
    private int aid;
    /**
     * 告警表 d_alarm_id
     */
    private String alarmId;

    transient private ApeAlarm parentApeAlarm;

    /**
     * 省公司代码
     */
    private ComCode comCode;
    /**
     * 该异常影响的前台页面应用id
     */
    private AppCode urlApp;
    /**
     * 该异常影响的前台微服务应用id，对应告警类型为AJAX性能|AJAX错误时非空
     */
    private AppCode ajaxApp;
    /**
     * 异常类型。页面完全加载时间|URL响应时间|AJAX性能|AJAX错误|JS错误
     */
    private AlarmSlaType alarmType;
    /**
     * 生成告警数据的时间
     */
    private LocalDateTime alarmTime;
    /**
     * 聚合数据开始时间
     */
    private LocalDateTime startTime;
    /**
     * 聚合数据结束时间
     */
    private LocalDateTime endTime;
    /**
     * 异常发生时间
     */
    private LocalDateTime detailTime;
    /**
     * 该异常影响的操作员
     */
    private String userCode;
    /**
     * 该异常影响的终端IP地址
     */
    private String ip;
    /**
     * 该异常影响的业务环节页面url或Ajax url
     */
    private String url;
    /**
     * 该告警影响的关键业务环节Ajax url，对应告警类型为AJAX性能|AJAX错误时非空
     */
    private String ajaxUrl;
    /**
     * 页面加载、url响应、Ajax响应性能 {"m_slow_time",xx} Ajax错误 {"d_msg",xx } JS错误 {"d_msg",xx,"d_filename",xx,"d_lineno",xx }
     */
    private JsonObject data;


    public ApeAlarmDetail() {

    }

    public int getParentAlarmId() {
        return parentApeAlarm == null ? 0 : parentApeAlarm.getId();
    }

    public String getData(String name) {
        return new GsonJsonObjectUtil(getData()).string(name, "");
    }

    public Number getNumberData(String name) {
        return new GsonJsonObjectUtil(getData()).number(name, 0);
    }

    public String getDataJson() {
        return getData().toString();
    }

    public Map<String, String> getDataMap() {
        Map<String, String> data = new LinkedHashMap<>();
        AlarmDataFieldTranslator instance = AlarmDataFieldTranslator.getInstance();
        for (Map.Entry<String, JsonElement> entry : getData().entrySet()) {
            AlarmDataFieldMapping mapping = instance.get(alarmType, entry.getKey());
            data.put(mapping.getCaption(), mapping.createFieldValue(entry.getValue().getAsString()));
        }
        return data;
    }

    public int getId() {
        return id;
    }

    public ApeAlarmDetail setId(int id) {
        this.id = id;
        return this;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public ApeAlarmDetail setAlarmId(String alarmId) {
        this.alarmId = alarmId;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public ApeAlarmDetail setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public ApeAlarmDetail setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public ApeAlarmDetail setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public AlarmSlaType getAlarmType() {
        return alarmType;
    }

    public ApeAlarmDetail setAlarmType(String alarmType) {
        this.alarmType = AlarmSlaType.parse(alarmType);
        return this;
    }

    public ApeAlarmDetail setAlarmType(AlarmSlaType alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public String getAlarmTypeName() {
        return alarmType == null ? null : alarmType.name();
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public ApeAlarmDetail setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public ApeAlarmDetail setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ApeAlarmDetail setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public LocalDateTime getDetailTime() {
        return detailTime;
    }

    public ApeAlarmDetail setDetailTime(LocalDateTime detailTime) {
        this.detailTime = detailTime;
        return this;
    }

    public String getUserCode() {
        return userCode;
    }

    public ApeAlarmDetail setUserCode(String userCode) {
        this.userCode = userCode;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public ApeAlarmDetail setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ApeAlarmDetail setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public ApeAlarmDetail setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    public JsonObject getData() {
        if (data == null) this.data = new JsonObject();
        return data;
    }

    public ApeAlarmDetail setData(String data) {
        this.data = new GsonJsonObjectUtil(data).get();
        return this;
    }

    public ApeAlarmDetail setData(JsonObject data) {
        this.data = data;
        return this;
    }

    public String getDataJsonFormatted() {
        return new GsonJsonObjectUtil(getData()).pretty();
    }

    public ApeAlarm getParentAlarm() {
        return parentApeAlarm;
    }

    public ApeAlarmDetail setParentAlarm(ApeAlarm parentApeAlarm) {
        this.parentApeAlarm = parentApeAlarm;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApeAlarmDetail that = (ApeAlarmDetail) o;
        return id >= 0 && id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
