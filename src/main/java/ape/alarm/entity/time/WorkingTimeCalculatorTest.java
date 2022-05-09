package ape.alarm.entity.time;

import ape.master.entity.code.ComCode;
import ape.master.entity.code.GeneralVariable;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WorkingTimeCalculatorTest {

    private static final ComCode General_COMCODE_OBJECT = GeneralVariable.COMCODE_OBJECT;
    private static WorkingTimeCalculator calculator;
    private static ComCode xinjiang;
    private static ComCode jiangsu;

    @BeforeAll
    static void beforeAll() {
        xinjiang = new ComCode().setId("65000000").setName("新疆分公司").setCloud("北云");
        jiangsu = new ComCode().setId("32000000").setName("江苏分公司").setCloud("北云");

        List<AlarmWeekDays> weekDays = List.of(
                new AlarmWeekDays().setId(1).setName("全国工作时间").setComCode(General_COMCODE_OBJECT)
                        .setEffective(true).setWeekDays(Set.of(1, 2, 3, 4, 5))
                        .setStartTime(LocalTime.of(9, 0)).setEndTime(LocalTime.of(17, 0)),
                new AlarmWeekDays().setId(2).setName("新疆工作时间").setComCode(xinjiang)
                        .setEffective(true).setWeekDays(Set.of(1, 2, 3, 4, 5))
                        .setStartTime(LocalTime.of(12, 0)).setEndTime(LocalTime.of(20, 0))
        );
        List<AlarmSpecialDay> specialDays = List.of(
                new AlarmSpecialDay().setId(3).setEffective(true).setVacation(true).setComCode(General_COMCODE_OBJECT)
                        .setStartTime(LocalDateTime.of(2021, 10, 1, 0, 0, 0))
                        .setEndTime(LocalDateTime.of(2021, 10, 7, 23, 59, 59)),
                new AlarmSpecialDay().setId(4).setEffective(true).setVacation(false).setComCode(xinjiang)
                        .setStartTime(LocalDateTime.of(2021, 10, 1, 0, 0, 0))
                        .setEndTime(LocalDateTime.of(2021, 10, 7, 23, 59, 59))
        );
        calculator = new WorkingTimeCalculator(weekDays, specialDays);
    }

    @Test
    void isWorkingTime() {
        LocalDateTime time;
        time = LocalDateTime.now();
        print(xinjiang, time);
        print(jiangsu, time);
        time = LocalDateTime.now().plusHours(3);
        print(xinjiang, time);
        print(jiangsu, time);
        time = LocalDateTime.of(2021, 10, 2, 12, 30, 0);
        print(xinjiang, time);
        print(jiangsu, time);
    }

    private void print(ComCode comCode, LocalDateTime localDateTime) {
        System.out.printf("%s in %s is %s.%n",
                LocalDateTimeFormatter.Short(localDateTime), comCode.getName(),
                calculator.isWorkingTime(comCode, localDateTime) ? "working time" : "vacation"
        );
    }

    @Test
    void listWorkingTimesByDay() {
        calculator.listWorkingTimesByHalfHours(xinjiang, LocalDate.now(), LocalDate.now())
                .forEach((time, workingTime) -> System.out.printf("%s in %s is %s.%n",
                        time, xinjiang.getName(),
                        workingTime ? "working time" : "vacation"
                ));
    }

    @Test
    void listWorkingTimesByDay1() {
        int interval = 30;
        int i = 24 * 60 / interval;
        System.out.println(i);
        List<LocalTime> localTimes = IntStream.range(0, i)
                .mapToObj(minutesToAdd -> LocalTime.MIN.plusMinutes(Math.min(minutesToAdd * interval, 24 * 60)))
                .collect(Collectors.toList());
        System.out.println(localTimes);
    }
}
