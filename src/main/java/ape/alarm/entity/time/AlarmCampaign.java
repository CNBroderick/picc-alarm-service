package ape.alarm.entity.time;

import ape.master.entity.code.ComCode;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AlarmCampaign implements Serializable {

    private int id;
    /**
     * 该活动适用省公司（全国适用的用特殊代码标识AAAAAAAA）。
     */
    private ComCode comCode;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动开始时间
     */
    private LocalDateTime startTime;
    /**
     * 活动结束时间
     */
    private LocalDateTime endTime;
    /**
     * 告警标志。1 告警，0 不告警
     */
    private boolean alarm;
    /**
     * 有效标志。1 有效，0 无效
     */
    private boolean effective;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    private AlarmCampaignStatusEnum status;

    public String getComcodeId() {
        return comCode == null ? null : comCode.getId();
    }

    public int getId() {
        return id;
    }

    public AlarmCampaign setId(int id) {
        this.id = id;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmCampaign setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlarmCampaign setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AlarmCampaign setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmCampaign setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public AlarmCampaign setAlarm(boolean alarm) {
        this.alarm = alarm;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmCampaign setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmCampaign setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public AlarmCampaignStatusEnum getStatus() {
        return status;
    }

    public AlarmCampaign setStatus(AlarmCampaignStatusEnum status) {
        this.status = status;
        return this;
    }

    public AlarmCampaign setStatus(int status) {
        return setStatus(AlarmCampaignStatusEnum.parse(status));
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
