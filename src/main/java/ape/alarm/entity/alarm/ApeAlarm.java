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
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Accessors(chain = true)
public class ApeAlarm implements HasComCode<ApeAlarm>, HasAjaxApp<ApeAlarm>, HasUrlApp<ApeAlarm> {

    /**
     * 告警唯一id
     */
    private int id = -1;
    /**
     * 告警id
     */
    private String alarmId;
    /**
     * 省公司代码
     */
    private ComCode comCode;
    /**
     * 告警类型
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
     * 该告警影响的前台页面应用id
     */
    private AppCode urlApp;
    /**
     * 该告警影响的业务环节页面url或Ajax url
     */
    private String url;
    /**
     * 该告警影响的前台微服务应用id，对应告警类型为AJAX性能|AJAX错误时非空。
     */
    private AppCode ajaxApp;
    /**
     * 该告警影响的前台微服务应用id，对应告警类型为AJAX性能|AJAX错误时非空
     */
    private String ajaxUrl;
    /**
     * 页面加载、url响应、Ajax响应性能： {"m_cnt_total",xx,"m_cnt_slow",xx, "m_slow_rate",xx,"m_ip_total",xx,"m_ip_slow",xx} JS错误、Ajax错误码、Ajax接口错误： {"m_cnt_total",xx,"m_cnt_error",xx, "m_error_rate",xx,"m_ip_total",xx,"m_ip_error",xx}
     */
    private JsonObject data;
    /**
     * 将告警信息发送给相关责任人的时间。空值表示尚未发送。根据客户需求，发送过程可以对告警适当聚合
     */
    private LocalDateTime sendTime;
    /**
     * 告警邮件的发送状态。值为待发送、发送中、已发送、发送失败。
     */
    private ApeAlarmSendStatus sendStatus;

    /**
     * 告警恢复时间
     */
    private LocalDateTime restoredTime;

    @Expose
    transient private Set<ApeAlarmDetail> apeAlarmDetails;

    public ApeAlarm() {

    }

    public boolean restored() {
        return restoredTime != null;
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

    public String getAlarmId() {
        return alarmId;
    }

    public ApeAlarm setAlarmId(String alarmId) {
        this.alarmId = alarmId;
        return this;
    }

    public String getDataJsonFormatted() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(getData());
    }

    public int getId() {
        return id;
    }

    public ApeAlarm setId(int id) {
        this.id = id;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public ApeAlarm setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public AlarmSlaType getAlarmType() {
        return alarmType;
    }

    public ApeAlarm setAlarmType(String alarmType) {
        this.alarmType = AlarmSlaType.parse(alarmType);
        return this;
    }

    public ApeAlarm setAlarmType(AlarmSlaType alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public String getAlarmTypeName() {
        return alarmType == null ? null : alarmType.name();
    }

    public LocalDateTime getAlarmTime() {
        return alarmTime;
    }

    public ApeAlarm setAlarmTime(LocalDateTime alarmTime) {
        this.alarmTime = alarmTime;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public ApeAlarm setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ApeAlarm setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public ApeAlarm setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public ApeAlarm setUrl(String url) {
        this.url = url;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public ApeAlarm setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public ApeAlarm setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    public JsonObject getData() {
        if (data == null) this.data = new JsonObject();
        return data;
    }

    public ApeAlarm setData(String data) {
        this.data = new GsonJsonObjectUtil(data).get();
        return this;
    }

    public ApeAlarm setData(JsonObject data) {
        this.data = data;
        return this;
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

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public ApeAlarm setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public String getSendStatus() {
        return getSendStatusType().name();
    }

    public ApeAlarm setSendStatus(String sendStatus) {
        this.sendStatus = ApeAlarmSendStatus.parse(sendStatus);
        return this;
    }

    public ApeAlarm setSendStatus(ApeAlarmSendStatus sendStatus) {
        this.sendStatus = sendStatus;
        return this;
    }

    public ApeAlarmSendStatus getSendStatusType() {
        if (sendStatus == null) this.sendStatus = ApeAlarmSendStatus.其他;
        return sendStatus;
    }

    public Set<ApeAlarmDetail> getAlarmDetails() {
        if (apeAlarmDetails == null) this.apeAlarmDetails = new LinkedHashSet<>();
        return apeAlarmDetails;
    }

    public ApeAlarm setAlarmDetails(Set<ApeAlarmDetail> apeAlarmDetails) {
        this.apeAlarmDetails = apeAlarmDetails;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApeAlarm apeAlarm = (ApeAlarm) o;
        return id > -1 && id == apeAlarm.id && Objects.equals(alarmId, ((ApeAlarm) o).alarmId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, alarmId);
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }
}
