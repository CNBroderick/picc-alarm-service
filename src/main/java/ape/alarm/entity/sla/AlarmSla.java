package ape.alarm.entity.sla;

import ape.master.entity.alarm.sla.AlarmSlaType;
import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.bklab.quark.util.security.ExamineUtil;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class AlarmSla implements Serializable, Comparable<AlarmSla> {

    /**
     * "阈值唯一ID。自动生成"
     */
    private int id = -1;

    /**
     * "阈值适用省公司。空值表示该阈值适用所有省公司。 "
     */
    private ComCode comCode;

    /**
     * "页面url所属的前台页面应用id，或者调用Ajax url的页面前台页面应用id。 "
     */
    private AppCode urlApp;

    /**
     * "Ajax url对应前台微服务id。url类型为页面url时该字段为空；"
     */
    private AppCode ajaxApp;

    /**
     * "阈值适用的开始时间。空值表示适用所有时间范围   "
     */
    private LocalDateTime startTime;

    /**
     * "阈值适用的结束时间。空值表示适用所有时间范围 "
     */
    private LocalDateTime endTime;

    /**
     * "阈值适用的告警类型。  页面完全加载时间|URL响应时间|AJAX性能|AJAX错误|JS错误"
     */
    private String alarmType;

    /**
     * "关键页面URL（d_alarm_type=|JS错误|JS错误）、Ajax URL（d_alarm_type= AJAX性能|AJAX错误）"
     */
    private String url;

    /**
     * "关键Ajax URL"
     */
    private String ajaxUrl;

    /**
     * <table>
     *     <caption>性能/错误告警阈值</caption>
     *     <tr><td>值含义</td><td>告警类型</td></tr>
     *     <tr><td>慢速调用占比</td><td>页面完全加载时间、URL响应时间、AJAX性能类型</td></tr>
     *     <tr><td>AJAX错误率</td><td>AJAX错误</td></tr>
     *     <tr><td>JS错误</td><td>错误次数</td></tr>
     * </table>
     */
    private double alarmSla;

    /**
     * 最小访问量阈值。含义如下：对页面完全加载时间、URL响应时间和AJAX性能类型的告警，页面/Ajax访问量达到该最小访问量阈值&慢速调用占比达到异常占比阈值时产生告警；对AJAX错误，Ajax访问量达到该最小访问量阈值&错误率达到异常占比阈值时产生告警；对JS错误，JS错误发生时所在页面访问量达到该最小访问量阈值&JS错误次数达到此值时产生告警
     */
    private int alarmNum;

    /**
     * "该url需要纳入监控的JS错误、Ajax错误\n[{"d_msg",xx },{"d_msg",xx },{"d_msg",xx }]"
     */
    private JsonArray error = new JsonArray();

    /**
     * "规则权重，UI在录入或修改时生成"
     */
    private int slaWeight;

    /**
     * "关键url生效标志。   1 有效，0 无效 "
     */
    private boolean effective = true;

    /**
     * "修改时间"
     */
    private LocalDateTime updateTime;


    public static List<AlarmSla> computeDeprecated(Collection<AlarmSla> alarmUrls) {
        List<AlarmSla> deprecated = new ArrayList<>(alarmUrls);
        deprecated.stream().max(Comparator.comparingLong(AlarmSla::getUpdateTimeLong).thenComparingInt(AlarmSla::getId))
                .ifPresent(deprecated::remove);
        return deprecated;
    }

    public boolean hasErrorType() {
        return AlarmSlaType.parse(alarmType).hasError();
    }

    public boolean hasAjaxType() {
        return AlarmSlaType.parse(alarmType).hasAjax();
    }

    public AlarmSlaType getAlarmSlaType() {
        return alarmType == null ? null : AlarmSlaType.parse(alarmType);
    }

    public String getAlarmSlaFormatted() {
        return Optional.ofNullable(AlarmSlaType.parse(alarmType)).map(a -> a.format(alarmSla)).orElse("");
    }

    public String getAlarmSlaLabel() {
        return Optional.ofNullable(AlarmSlaType.parse(alarmType)).map(AlarmSlaType::getLabel).orElse("");
    }

    public String getAlarmSlaSuffix() {
        return Optional.ofNullable(AlarmSlaType.parse(alarmType)).map(AlarmSlaType::getLabel).orElse("");
    }

    public String computeMainFiledSha512() {
        return ExamineUtil.sha_512()
                .calc(getComcodeId()
                      + alarmType
                      + getUrlAppId()
                      + url
                      + Objects.toString(getAjaxAppId(), "")
                      + Objects.toString(ajaxUrl, "")
                      + Objects.toString(getErrorJson(), "")
                );
    }

    public String computeMainPropertiesSha512() {
        return ExamineUtil.sha_512()
                .calc(getAlarmType()
                      + getUrlAppId()
                      + url
                      + Objects.toString(getAjaxAppId(), "")
                      + Objects.toString(ajaxUrl, "")
                      + Objects.toString(getErrorJson(), "")
                );
    }

    public String computeMainIndexSha512() {
        return ExamineUtil.sha_512()
                .calc(getComcodeId()
                      + getUrlAppId()
                      + url
                      + Objects.toString(getAjaxAppId(), "")
                      + Objects.toString(ajaxUrl, "")
                      + Objects.toString(getErrorJson(), "")
                );
    }

    @SuppressWarnings("DuplicatedCode")
    public int computeWeight() {
        int weight = 0;
        if (!Objects.equals(getComcodeId(), GeneralVariable.COMCODE)) weight += 10_0000;
        if (!Objects.equals(getUrlAppId(), GeneralVariable.URL_APP)) weight += 1_0000;
        if (!Objects.equals(url, GeneralVariable.URL)) weight += 1000;
        if (!Objects.equals(getAjaxAppId(), GeneralVariable.AJAX_APP)) weight += 100;
        if (!Objects.equals(ajaxUrl, GeneralVariable.AJAX_URL)) weight += 10;
        if (!Objects.equals(error, GeneralVariable.ERROR_JSON)) weight += 1;
        return weight;
    }

    @SuppressWarnings("DuplicatedCode")
    public int computeBinaryWeight() {
        int weight = 0;
        if (!Objects.equals(getComcodeId(), GeneralVariable.COMCODE)) weight += 0b10_0000;
        if (!Objects.equals(getUrlAppId(), GeneralVariable.URL_APP)) weight += 0b1_0000;
        if (!Objects.equals(url, GeneralVariable.URL)) weight += 0b1000;
        if (!Objects.equals(getAjaxAppId(), GeneralVariable.AJAX_APP)) weight += 0b100;
        if (!Objects.equals(ajaxUrl, GeneralVariable.AJAX_URL)) weight += 0b10;
        if (!Objects.equals(error, GeneralVariable.ERROR_JSON)) weight += 0b1;
        return weight;
    }

    public String getEffectiveTimeRangeFormatted() {
        if (startTime == null && endTime == null) return "永久";
        if (startTime != null && endTime == null) return "起始自 " + LocalDateTimeFormatter.Short(startTime);
        if (startTime == null) return "截止于 " + LocalDateTimeFormatter.Short(endTime);

        return "%s -- %s".formatted(LocalDateTimeFormatter.Short(startTime), LocalDateTimeFormatter.Short(endTime));
    }

    public String getComcodeId() {
        return Optional.ofNullable(comCode).map(ComCode::getId).orElse(null);
    }

    public String getUrlAppId() {
        return Optional.ofNullable(urlApp).map(AppCode::getId).orElse(null);
    }

    public String getAjaxAppId() {
        return Optional.ofNullable(ajaxApp).map(AppCode::getId).orElse(null);
    }

    public String getErrorJson() {
        return Optional.ofNullable(error).map(JsonElement::toString).orElse(new JsonArray().toString());
    }

    public long getUpdateTimeLong() {
        return Optional.ofNullable(updateTime).map(a -> a.toEpochSecond(ZoneOffset.ofHours(8))).orElse(0L);
    }

    public int getId() {
        return id;
    }

    public AlarmSla setId(int id) {
        this.id = id;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmSla setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public AlarmSla setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public AlarmSla setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public LocalDateTime getStartTime() {
        if (startTime == null) this.startTime = LocalDateTime.MIN;
        return startTime;
    }

    public AlarmSla setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmSla setEndTime(LocalDateTime endTime) {
        if (startTime == null) this.startTime = LocalDateTime.MAX;
        this.endTime = endTime;
        return this;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public AlarmSla setAlarmType(String alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AlarmSla setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public AlarmSla setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    public double getAlarmSla() {
        return alarmSla;
    }

    public AlarmSla setAlarmSla(double alarmSla) {
        this.alarmSla = alarmSla;
        return this;
    }

    public JsonArray getError() {
        if (error == null) this.error = new JsonArray();
        return error;
    }

    public AlarmSla setError(JsonArray error) {
        this.error = error;
        return this;
    }

    public AlarmSla setError(String error) {
        try {
            this.error = new Gson().fromJson(error, JsonArray.class);
        } catch (Exception ignore) {
            this.error = new JsonArray();
        }
        return this;
    }

    public int getSlaWeight() {
        return slaWeight;
    }

    public AlarmSla setSlaWeight(int slaWeight) {
        this.slaWeight = slaWeight;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmSla setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmSla setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }

    public String getErrorJsonFormatted() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(getError());
    }

    public int getAlarmNum() {
        return alarmNum;
    }

    public AlarmSla setAlarmNum(int alarmNum) {
        this.alarmNum = alarmNum;
        return this;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public int compareTo(AlarmSla alarmSla) {
        if (alarmSla == null) return 1;
        return Comparator
                .comparing(AlarmSla::getComcodeId)
                .thenComparing(AlarmSla::getAlarmType)
                .thenComparing(alarmSla1 -> Optional.ofNullable(alarmSla1.getUrlAppId()).orElse(""))
                .thenComparing(a -> Optional.ofNullable(a.getUrl()).orElse(""))
                .thenComparing(alarmSla1 -> Optional.ofNullable(alarmSla1.getAjaxAppId()).orElse(""))
                .thenComparing(a -> Optional.ofNullable(a.getAjaxUrl()).orElse(""))
                .thenComparing(a -> a.getError() != null ? 1 : 0)
                .thenComparing(a -> Optional.ofNullable(a.getStartTime()).orElse(LocalDateTime.MIN))
                .compare(this, alarmSla);
    }
}
