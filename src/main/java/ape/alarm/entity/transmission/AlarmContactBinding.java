package ape.alarm.entity.transmission;

import org.bklab.quark.util.json.GsonJsonObjectUtil;

import java.time.LocalDateTime;

public class AlarmContactBinding {

    /**
     * 告警通知策略id tb_alarm_notification_policy id
     */
    private int npId;
    /**
     * 告警联系人id
     */
    private int contactId;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


    public AlarmContactBinding() {

    }

    public int getNpId() {
        return npId;
    }

    public AlarmContactBinding setNpId(int npId) {
        this.npId = npId;
        return this;
    }

    public int getContactId() {
        return contactId;
    }

    public AlarmContactBinding setContactId(int contactId) {
        this.contactId = contactId;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmContactBinding setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
