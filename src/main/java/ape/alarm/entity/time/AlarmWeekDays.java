package ape.alarm.entity.time;

import ape.master.entity.code.AppCode;
import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import lombok.Data;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AlarmWeekDays implements Serializable, Comparable<AlarmWeekDays> {

    private int id = -1;
    @NotBlank
    private String name;
    private ComCode comCode;
    private AppCode appCode;
    private Set<Integer> weekDays = new LinkedHashSet<>();
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean effective = true;
    private LocalDateTime updateTime;

    public AlarmWeekDays() {
    }

    @Override
    public int compareTo(@NotNull AlarmWeekDays alarmWeekDays) {
        return Comparator
                .<AlarmWeekDays>comparingInt(a -> Objects.equals(GeneralVariable.COMCODE_OBJECT, a.getComCode()) ? 0 : 1)
                .thenComparingLong(AlarmWeekDays::getDurationLong)
                .thenComparing(AlarmWeekDays::getStartTime).compare(this, alarmWeekDays);
    }

    public long getDurationLong() {
        return getEndTime().toNanoOfDay() - getStartTime().toNanoOfDay();
    }

    public boolean isInRange(LocalDateTime target) {
        return isInRange(target.toLocalTime());
    }

    public boolean isInRange(LocalTime localTime) {
        return !localTime.isAfter(endTime) && !localTime.isBefore(startTime);
    }

    public boolean isWeekDay(int value) {
        return weekDays.contains(value);
    }

    public boolean isWeekDay(DayOfWeek dayOfWeek) {
        return weekDays.contains(dayOfWeek.getValue());
    }

    public int getId() {
        return id;
    }

    public AlarmWeekDays setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AlarmWeekDays setName(String name) {
        this.name = name;
        return this;
    }

    public ComCode getComCode() {
        return comCode;
    }

    public AlarmWeekDays setComCode(ComCode comCode) {
        this.comCode = comCode;
        return this;
    }

    public String getComCodeId() {
        return comCode == null ? null : comCode.getId();
    }

    public AppCode getAppCode() {
        return appCode;
    }

    public AlarmWeekDays setAppCode(AppCode appCode) {
        this.appCode = appCode;
        return this;
    }

    public String getAppCodeId() {
        return appCode == null ? null : appCode.getId();
    }

    public Set<Integer> getWeekDays() {
        if (weekDays == null) this.weekDays = new LinkedHashSet<>();
        return weekDays;
    }

    public AlarmWeekDays setWeekDays(String weekDays) {
        this.weekDays = new LinkedHashSet<>();
        try {

            JsonArray array = new Gson().fromJson(weekDays, JsonArray.class);
            array.forEach(a -> this.weekDays.add(a.getAsInt()));
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("解析JSON`" + weekDays + "`错误。", e);
        }
        return this;
    }

    public AlarmWeekDays setWeekDays(int... weekDays) {
        this.weekDays = new LinkedHashSet<>();
        for (int weekDay : weekDays) {
            this.weekDays.add(weekDay);
        }
        return this;
    }

    public AlarmWeekDays setWeekDays(Set<Integer> weekDays) {
        this.weekDays = weekDays;
        return this;
    }

    public Set<DayOfWeek> getDayOfWeeks() {
        return getWeekDays().stream().map(DayOfWeek::of).collect(Collectors.toSet());
    }

    public AlarmWeekDays setDayOfWeeks(Set<DayOfWeek> weekDays) {
        this.weekDays = weekDays.stream().map(DayOfWeek::getValue).collect(Collectors.toSet());
        return this;
    }

    public String getWeekDaysJson() {
        JsonArray array = new JsonArray();
        getWeekDays().forEach(array::add);
        return array.toString();
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public AlarmWeekDays setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        return this;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public AlarmWeekDays setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        return this;
    }

    public boolean isEffective() {
        return effective;
    }

    public AlarmWeekDays setEffective(boolean effective) {
        this.effective = effective;
        return this;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public AlarmWeekDays setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
