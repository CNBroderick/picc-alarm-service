package ape.alarm.entity.time;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Objects;

public class AlarmSpecialDay implements Serializable, Comparable<AlarmSpecialDay> {

    private int id;
    private String name;
    private ComCode comCode;
    private AppCode appCode;
    private boolean vacation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean effective;
    private LocalDateTime updateTime;

    @Override
    public int compareTo(@NotNull AlarmSpecialDay alarmSpecialDays) {
        return Comparator
                .<AlarmSpecialDay>comparingInt(a -> Objects.equals(GeneralVariable.COMCODE_OBJECT, a.getComCode()) ? 0 : 1)
                .thenComparingLong(AlarmSpecialDay::getDurationLong)
                .thenComparing(AlarmSpecialDay::getStartTime).compare(this, alarmSpecialDays);
    }

    public long getDurationLong() {
        return getEndTime().toEpochSecond(ZoneOffset.ofHours(8)) - getStartTime().toEpochSecond(ZoneOffset.ofHours(8));
    }

    public boolean isInRange(LocalDateTime localDateTime) {
        return !localDateTime.isAfter(endTime) && !localDateTime.isBefore(startTime);
    }

    public int getId() {
        return id;
    }

    public AlarmSpecialDay setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlarmSpecialDay setName(String name) {
        this.name = name;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmSpecialDay setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getComCodeId() {
        return comCode == null ? null : comCode.getId();
    }

    public AppCode getAppCode() {
        return appCode;
    }

    public AlarmSpecialDay setAppCode(AppCode appCode) {
        this.appCode = appCode;
        return this;
    }

    public String getAppCodeId() {
        return appCode == null ? null : appCode.getId();
    }

    public boolean isVacation() {
        return vacation;
    }

    public AlarmSpecialDay setVacation(boolean vacation) {
        this.vacation = vacation;
        return this;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public AlarmSpecialDay setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public AlarmSpecialDay setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmSpecialDay setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmSpecialDay setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
