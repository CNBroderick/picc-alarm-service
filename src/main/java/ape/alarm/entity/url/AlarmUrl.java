package ape.alarm.entity.url;


import ape.alarm.entity.common.HasAjaxApp;
import ape.alarm.entity.common.HasComCode;
import ape.alarm.entity.common.HasUrlApp;
import ape.alarm.entity.sla.AlarmSla;
import ape.master.entity.alarm.url.AlarmUrlType;
import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import org.bklab.quark.util.security.ExamineUtil;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

public class AlarmUrl implements Serializable, Comparable<AlarmUrl>, HasComCode<AlarmUrl>, HasAjaxApp<AlarmUrl>, HasUrlApp<AlarmUrl> {

    public static final String NATIONAL_COMCODE = GeneralVariable.COMCODE;

    private final List<AlarmUrl> children = new ArrayList<>();
    private int id = -1;
    private int parentId = -1;
    private AlarmUrl parent;
    private ComCode comCode;
    private AppCode urlApp;
    private AppCode ajaxApp;
    private String url;
    private String ajaxUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private JsonArray error = new JsonArray();
    private boolean alarm = true;
    private boolean effective = true;
    private LocalDateTime updateTime;
    private AlarmUrlType urlType;

    public static String getNationalComcode() {
        return NATIONAL_COMCODE;
    }

    public static List<AlarmUrl> computeDeprecated(Collection<AlarmUrl> alarmUrls) {
        List<AlarmUrl> deprecated = new ArrayList<>(alarmUrls);
        deprecated.stream().max(Comparator.comparingLong(AlarmUrl::getUpdateTimeLong).thenComparingInt(AlarmUrl::getId))
                .ifPresent(deprecated::remove);
        return deprecated;
    }

    public boolean hasErrorType() {
        return urlType.hasError();
    }

    public boolean hasAjaxType() {
        return urlType.hasAjax();
    }

    public boolean containGeneral() {
        return Objects.equals(urlApp, GeneralVariable.URL_APP_OBJECT)
               || Objects.equals(url, GeneralVariable.URL)
               || Objects.equals(ajaxApp, GeneralVariable.AJAX_APP_OBJECT)
               || Objects.equals(ajaxUrl, GeneralVariable.AJAX_URL)
               || Objects.equals(error, GeneralVariable.ERROR_JSON);
    }

    @Deprecated
    public AlarmUrl createChild() {
        AlarmUrl alarmUrl = new AlarmUrl();
        alarmUrl.parent = this;
        alarmUrl.parentId = id;
        alarmUrl.comCode = comCode;
        alarmUrl.urlApp = urlApp;
        alarmUrl.ajaxApp = ajaxApp;
        alarmUrl.url = url;
        alarmUrl.ajaxUrl = ajaxUrl;
        alarmUrl.startTime = startTime;
        alarmUrl.endTime = endTime;
        alarmUrl.error = error;
        alarmUrl.alarm = alarm;
        alarmUrl.updateTime = updateTime;
        alarmUrl.effective = effective;
        this.children.add(alarmUrl);
        return alarmUrl;
    }

    public AlarmSla createAlarmSla() {
        AlarmSla alarmSla = new AlarmSla();
        alarmSla.setId(-1);
        alarmSla.setComCode(comCode);
        alarmSla.setUrlApp(urlApp);
        alarmSla.setAjaxApp(ajaxApp);
        alarmSla.setStartTime(startTime);
        alarmSla.setEndTime(endTime);
        alarmSla.setUrl(url);
        alarmSla.setAjaxUrl(ajaxUrl);
        alarmSla.setError(error);
        alarmSla.setEffective(true);
        alarmSla.setUpdateTime(LocalDateTime.now());
        alarmSla.computeBinaryWeight();
        return alarmSla;
    }

    public String computeMainFiledSha512() {
        return ExamineUtil.sha_512()
                .calc(getComcodeId()
                      + getUrlTypeValue()
                      + getUrlAppId()
                      + url
                      + Objects.toString(getAjaxAppId(), "")
                      + Objects.toString(ajaxUrl, "")
                      + Objects.toString(getErrorJson(), "")
                );
    }

    public String computeMainPropertiesSha512() {
        return ExamineUtil.sha_512()
                .calc(getUrlTypeValue()
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

    public boolean isPageUrlType() {
        return ajaxUrl == null;
    }

    public boolean isAjaxUrlType() {
        return ajaxUrl != null;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public AlarmUrl setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    public boolean isNationalComCode() {
        return Objects.equals(getComcodeId(), NATIONAL_COMCODE);
    }

    public boolean isProvinceComCode() {
        return !isNationalComCode();
    }

    public AlarmUrlLevel getLevel() {
        if (startTime != null || endTime != null) return AlarmUrlLevel.TIME;
        if (url != null) return AlarmUrlLevel.URL;
        if (ajaxApp != null || urlApp != null) return AlarmUrlLevel.URL_APP;
        if (comCode != null) return AlarmUrlLevel.PROVINCE;
        return AlarmUrlLevel.ROOT;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public int compareTo(@NotNull AlarmUrl alarmUrl) {
        if (alarmUrl == null) return 1;
        return Comparator
                .comparing(AlarmUrl::getComcodeId)
                .thenComparing(AlarmUrl::getUrlType)
                .thenComparing(AlarmUrl::getUrlAppId)
                .thenComparing(a -> Optional.ofNullable(a.getUrl()).orElse(""))
                .thenComparing(AlarmUrl::getAjaxAppId)
                .thenComparing(a -> Optional.ofNullable(a.getAjaxUrl()).orElse(""))
                .thenComparing(a -> a.getError() == null ? 0 : 1)
                .thenComparing(a -> Optional.ofNullable(a.getStartTime()).orElse(LocalDateTime.MIN))
                .compare(this, alarmUrl);
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

    public AlarmUrl addChildren(AlarmUrl children) {
        this.children.add(children);
        children.setParent(this);
        return this;
    }

    public int getId() {
        return id;
    }

    public AlarmUrl setId(int id) {
        this.id = id;
        return this;
    }

    public int getParentId() {
        return parentId;
    }

    public AlarmUrl setParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public AlarmUrl getParent() {
        return parent;
    }

    public AlarmUrl setParent(AlarmUrl parent) {
        this.parent = parent;
        this.parentId = parent == null ? -1 : parent.getParentId();
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmUrl setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public AlarmUrl setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public AlarmUrl setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AlarmUrl setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmUrl setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AlarmUrl setUrl(String url) {
        this.url = url;
        return this;
    }

    public JsonArray getError() {
        return error;
    }

    public AlarmUrl setError(JsonArray error) {
        this.error = error;
        return this;
    }

    public AlarmUrl setError(String error) {
        try {
            this.error = new Gson().fromJson(error, JsonArray.class);
        } catch (Exception e) {
            this.error = new JsonArray();
            LoggerFactory.getLogger(getClass()).error("tb_alarm_url id=" + id + " 存在非法的JSON ARRAY格式：" + error, e);
            throw new RuntimeException("tb_alarm_url id=" + id + " 存在非法的JSON ARRAY格式：" + error, e);
        }
        return this;
    }

    public String getErrorJson() {
        return Optional.ofNullable(error).orElseGet(JsonArray::new).toString();
    }

    public String getErrorJsonFormatted() {
        return Optional.ofNullable(error)
                .map(a -> new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(a))
                .orElse("[\n\n]");
    }

    public boolean isAlarm() {
        return alarm;
    }

    public AlarmUrl setAlarm(boolean alarm) {
        this.alarm = alarm;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmUrl setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Long getUpdateTimeLong() {
        return updateTime == null ? 0 : updateTime.toEpochSecond(ZoneOffset.ofHours(8));
    }

    public List<AlarmUrl> getChildren() {
        return children;
    }

    public List<AlarmUrl> getSortedChildren() {
        return children.stream().sorted().collect(Collectors.toUnmodifiableList());
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmUrl setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public AlarmUrlType getUrlType() {
        return urlType;
    }

    public AlarmUrl setUrlType(String urlType) {
        return setUrlType(AlarmUrlType.parse(urlType));
    }

    public AlarmUrl setUrlType(AlarmUrlType urlType) {
        this.urlType = urlType;
        return this;
    }

    public String getUrlTypeValue() {
        return urlType == null ? null : urlType.name();
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
