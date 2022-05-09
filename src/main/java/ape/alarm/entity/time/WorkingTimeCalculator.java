package ape.alarm.entity.time;

import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import org.bklab.quark.util.time.LocalDateRange;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.bklab.quark.util.time.LocalDateTimeRange;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class WorkingTimeCalculator {

    private static final ComCode General_COMCODE_OBJECT = GeneralVariable.COMCODE_OBJECT;
    private final Collection<AlarmWeekDays> alarmWeekDays;
    private final Collection<AlarmSpecialDay> alarmSpecialDays;

    public WorkingTimeCalculator(Collection<AlarmWeekDays> alarmWeekDays, Collection<AlarmSpecialDay> alarmSpecialDays) {
        this.alarmWeekDays = alarmWeekDays.stream().filter(Objects::nonNull).filter(AlarmWeekDays::isEffective).collect(Collectors.toUnmodifiableList());
        this.alarmSpecialDays = alarmSpecialDays.stream().filter(Objects::nonNull).filter(AlarmSpecialDay::isEffective).collect(Collectors.toUnmodifiableList());
    }

    /**
     * 工作日规则：
     * 1. 如果分省创建了1条工作日规则，那么全国规则自动失效。
     * 2. 如果有多条规则作用于同一时间，优先顺序为 时间范围最小的、开始时间最晚的、id最大的。
     * <p>
     * 特殊日期规则：
     * 1. 如果分省和全国都创建了规则，那么他们都生效。
     * 2. 如果同一天分省和全国都创建了规则，那么取分省的规则。
     * 3. 如果存在多条规则应用于该天，优先顺序为：取开始时间最晚的、id最大的一条。
     *
     * @param comCode       机构代码
     * @param localDateTime 查询时间
     *
     * @return 输入的查询时间是否为工作时间
     */
    public boolean isWorkingTime(ComCode comCode, LocalDateTime localDateTime) {
        AlarmSpecialDay effectSpecialDayRules = getEffectSpecialDayRules(comCode, localDateTime);

        if (effectSpecialDayRules != null) {
            return !effectSpecialDayRules.isVacation();
        }

        AlarmWeekDays effectWeekDayRules = getEffectWeekDayRules(comCode, localDateTime);
        return effectWeekDayRules != null && effectWeekDayRules.isWeekDay(localDateTime.getDayOfWeek());
    }

    public Map<String, Boolean> listWorkingTimesByHalfHours(ComCode comCode, LocalDate startDate, LocalDate endDate) {
        LocalDateRange range = new LocalDateRange(startDate, endDate);
        return listWorkingTimesByHalfHours(comCode, LocalDateTime.of(range.getStart(), LocalTime.MIN), LocalDateTime.of(range.getEnd(), LocalTime.MAX));
    }

    public Map<String, Boolean> listWorkingTimesByHalfHours(ComCode comCode, LocalDateTime startTime, LocalDateTime endTime) {
        return listWorkingTimesByHalfHours(comCode, startTime, endTime, Duration.ofMinutes(30));
    }

    public Map<String, Boolean> listWorkingTimesByHalfHours(ComCode comCode, LocalDateTime startTime, LocalDateTime endTime, Duration duration) {
        return new LocalDateTimeRange(startTime, endTime).getIncludes(duration).stream()
                .collect(Collectors.toMap(LocalDateTimeFormatter::Short, a -> isWorkingTime(comCode, a), (a, b) -> b, LinkedHashMap::new));
    }

    /**
     * 特殊日期规则：
     * 1. 如果分省和全国都创建了规则，那么他们都生效。
     * 2. 如果同一天分省和全国都创建了规则，那么取分省的规则。
     * 3. 如果存在多条规则应用于该时间，优先顺序为：取开始时间最晚的、id最大的一条。
     *
     * @param localDateTime 查询日期
     *
     * @return 该日期的有效规则
     */
    public AlarmSpecialDay getEffectSpecialDayRules(ComCode comCode, LocalDateTime localDateTime) {
        return alarmSpecialDays.stream()
                .filter(a -> Objects.equals(a.getComCode(), comCode) || Objects.equals(a.getComCode(), General_COMCODE_OBJECT))
                .filter(a -> a.isInRange(localDateTime))
                .min(
                        Comparator.<AlarmSpecialDay>comparingInt(a -> GeneralVariable.COMCODE.equals(comCode.getId()) ? 1 : 0)
                                .thenComparingLong(a -> Duration.between(a.getStartTime(), localDateTime).getSeconds())
                                .thenComparingInt(a -> -1 * a.getId())
                ).orElse(null);
    }

    /**
     * 工作日规则：
     * 1. 如果分省创建了1条工作日规则，那么全国规则自动失效。
     * 2. 如果有多条规则作用于同一时间，优先顺序为 时间范围最小的、开始时间最晚的、id最大的。
     *
     * @param localDateTime 查询日期
     *
     * @return 该日期的有效规则
     */
    public AlarmWeekDays getEffectWeekDayRules(ComCode comCode, LocalDateTime localDateTime) {
        List<AlarmWeekDays> rules = alarmWeekDays.stream()
                .filter(a -> Objects.equals(a.getComCode(), comCode))
                .collect(Collectors.toUnmodifiableList());
        if (rules.isEmpty()) {
            rules = alarmWeekDays.stream()
                    .filter(a -> Objects.equals(a.getComCode(), General_COMCODE_OBJECT))
                    .collect(Collectors.toUnmodifiableList());
        }
        if (rules.isEmpty()) return null;

        return rules.stream()
                .filter(a -> a.isInRange(localDateTime))
                .min(
                        Comparator.comparingLong(AlarmWeekDays::getDurationLong)
                                .thenComparingLong(a -> Duration.between(a.getStartTime(), localDateTime).getSeconds())
                                .thenComparingInt(a -> -1 * a.getId())
                ).orElse(null);
    }

}
