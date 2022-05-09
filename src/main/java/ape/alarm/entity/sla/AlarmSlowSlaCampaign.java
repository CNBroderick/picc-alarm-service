package ape.alarm.entity.sla;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.time.LocalDateTime;

public class AlarmSlowSlaCampaign {

    private int id;
    /**
     * 阈值的适用省公司。
     */
    private ComCode comCode;
    /**
     * 阈值的适用活动名称。
     */
    private String campaignName;
    /**
     * 页面url所属的前台页面应用id，或者调用Ajax url的页面前台页面应用id。
     */
    private AppCode urlApp;
    /**
     * Ajax url对应前台微服务id，对应告警类型为AJAX性能非空
     */
    private AppCode ajaxApp;
    /**
     * 阈值适用的开始时间。
     */
    private LocalDateTime startTime;
    /**
     * 阈值适用的结束时间。
     */
    private LocalDateTime endTime;
    /**
     * 阈值适用的告警类型。  页面加载时间|URL响应时间|AJAX响应时间
     */
    private String alarmType;
    /**
     * AjaxURL，对应告警类型为AJAX性能时非空
     */
    private String ajaxUrl;
    /**
     * 页面URL
     */
    private String url;
    /**
     * 慢速阈值。单位毫秒。含义如下：<br/>
     * 当告警类型是页面完全加载时间时，如果页面完全加载时间(m_loaded)大于此值时则认为该页面为慢速。<br/>
     * 当告警类型是对URL响应时间时，如果URL响应时间(m_httpTime)大于此值则认为该调用为慢速 。<br/>
     * 当告警类型是对AJAX性能时，如果AJAX执行时间（m_duration）大于此值则认为该调用为慢速 。<br/>
     */
    private int sla;
    /**
     * 页面完全加载时间/URL响应时间/AJAX执行时间均值
     */
    private double avg;
    /**
     * 页面完全加载时间/URL响应时间/AJAX执行时间下四分位数
     */
    private double quartile1;
    /**
     * 页面完全加载时间/URL响应时间/AJAX执行时间中位数
     */
    private double quartile2;
    /**
     * 页面完全加载时间/URL响应时间/AJAX执行时间上四分位数
     */
    private double quartile3;
    /**
     * 有效标志。1=有效，0=无效
     */
    private boolean effective;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


    public AlarmSlowSlaCampaign() {

    }

    public int getId() {
        return id;
    }

    public AlarmSlowSlaCampaign setId(int id) {
        this.id = id;
        return this;
    }

    public String getComCodeId() {
        return comCode == null ? null : comCode.getId();
    }

    public String getUrlAppId() {
        return urlApp == null ? null : urlApp.getId();
    }

    public String getAjaxAppId() {
        return ajaxApp == null ? null : ajaxApp.getId();
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmSlowSlaCampaign setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public AlarmSlowSlaCampaign setCampaignName(String campaignName) {
        this.campaignName = campaignName;
        return this;
    }

    public AppCode getUrlApp() {
        return urlApp;
    }

    public AlarmSlowSlaCampaign setUrlApp(AppCode urlApp) {
        this.urlApp = urlApp;
        return this;
    }

    public AppCode getAjaxApp() {
        return ajaxApp;
    }

    public AlarmSlowSlaCampaign setAjaxApp(AppCode ajaxApp) {
        this.ajaxApp = ajaxApp;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AlarmSlowSlaCampaign setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmSlowSlaCampaign setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public AlarmSlowSlaCampaign setAlarmType(String alarmType) {
        this.alarmType = alarmType;
        return this;
    }

    public String getAjaxUrl() {
        return ajaxUrl;
    }

    public AlarmSlowSlaCampaign setAjaxUrl(String ajaxUrl) {
        this.ajaxUrl = ajaxUrl;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public AlarmSlowSlaCampaign setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getSla() {
        return sla;
    }

    public AlarmSlowSlaCampaign setSla(int sla) {
        this.sla = sla;
        return this;
    }

    public double getAvg() {
        return avg;
    }

    public AlarmSlowSlaCampaign setAvg(double avg) {
        this.avg = avg;
        return this;
    }

    public double getQuartile1() {
        return quartile1;
    }

    public AlarmSlowSlaCampaign setQuartile1(double quartile1) {
        this.quartile1 = quartile1;
        return this;
    }

    public double getQuartile2() {
        return quartile2;
    }

    public AlarmSlowSlaCampaign setQuartile2(double quartile2) {
        this.quartile2 = quartile2;
        return this;
    }

    public double getQuartile3() {
        return quartile3;
    }

    public AlarmSlowSlaCampaign setQuartile3(double quartile3) {
        this.quartile3 = quartile3;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmSlowSlaCampaign setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmSlowSlaCampaign setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
