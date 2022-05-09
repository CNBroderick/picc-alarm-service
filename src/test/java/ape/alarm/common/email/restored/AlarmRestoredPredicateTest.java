package ape.alarm.common.email.restored;

import ape.master.common.util.ApeAlarmIdFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class AlarmRestoredPredicateTest {

    void apply1() {
        System.out.println(ApeAlarmIdFactory.create("Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f",
                LocalDateTime.now().minusMinutes(26), LocalDateTime.now().minusMinutes(21)
        ).compute().getAlarmId());
        System.out.println(ApeAlarmIdFactory.create("Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f",
                LocalDateTime.now().minusMinutes(16), LocalDateTime.now().minusMinutes(11)
        ).compute().getAlarmId());
        System.out.println(ApeAlarmIdFactory.create("Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f",
                LocalDateTime.now().minusMinutes(6), LocalDateTime.now().minusMinutes(1)
        ).compute().getAlarmId());
    }

    void apply() {
        new AlarmRestoredPredicate(Duration.ofMinutes(15)).apply(List.of(
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2oun996c+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2oun99k8+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2oun99y4+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2ounuoe8+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2ounuos4+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2ounup60+8c",

                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2owas65t+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2owas6xl+8c",
                "Bi6sb7dvfasKZJfurWkEq9OruRqjxzrLmzsBTBs2N9f#2owas7pd+8c"
        )).forEach((alarmId, time) -> System.out.println(alarmId + " : " + time));
    }
}
