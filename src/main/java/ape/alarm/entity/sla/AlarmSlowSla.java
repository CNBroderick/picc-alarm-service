package ape.alarm.entity.sla;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.Optional;

public class AlarmSlowSla {

    private int id;
    private ComCode comCode;
    private AppCode urlApp;
    private AppCode ajaxApp;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String alarmType;
    private String url;
    private String ajaxUrl;
    private int sla;
    private double avg;
    private double quartile1;
    private double quartile2;
    private double quartile3;
    private boolean effective;
    private LocalDateTime updateTime;


    public String getComcodeId() {
        return Optional.ofNullable(comCode).map(ComCode::getId).orElse(null);
    }

    public String getUrlAppId() {
        return Optional.ofNullable(urlApp).map(AppCode::getId).orElse(null);
    }

    public String getAjaxAppId() {
        return Optional.ofNullable(ajaxApp).map(AppCode::getId).orElse(null);
    }


    public int getId() {
        return id;
    }

    public AlarmSlowSla setId(int id) {
        this.id = id;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmSlowSla setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public AlarmSlowSla setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public AlarmSlowSla setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AlarmSlowSla setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmSlowSla setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public AlarmSlowSla setAlarmType(String alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AlarmSlowSla setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getSla() {
        return sla;
    }

    public AlarmSlowSla setSla(int sla) {
        this.sla = sla;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public AlarmSlowSla setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    public double getQuartile1() {
        return quartile1;
    }

    public AlarmSlowSla setQuartile1(double quartile1) {
        this.quartile1 = quartile1;
        return this;
    }

    public double getQuartile2() {
        return quartile2;
    }

    public AlarmSlowSla setQuartile2(double quartile2) {
        this.quartile2 = quartile2;
        return this;
    }

    public double getQuartile3() {
        return quartile3;
    }

    public AlarmSlowSla setQuartile3(double quartile3) {
        this.quartile3 = quartile3;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmSlowSla setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmSlowSla setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public AlarmSlowSla setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }

}
