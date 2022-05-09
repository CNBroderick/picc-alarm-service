package ape.alarm.operation.jdbc.alarm;

import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;

public class ApeAlarmQueryTest {

    public void doExecute() throws Exception {

        for (AlarmSendLog a : new ApeOperationBuilder().add("aid", 8089).<AlarmSendLog>executeQueryList(ApeAlarmOperationEnum.AlarmSendLogQuery)) {
            System.out.println(a.getAlarmId());
            System.out.println(a.getAlarm());
        }

    }
}
