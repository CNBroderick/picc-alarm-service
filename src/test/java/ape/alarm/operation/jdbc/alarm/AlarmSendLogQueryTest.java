package ape.alarm.operation.jdbc.alarm;

import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarmDetail;
import ape.master.operation.ApeConnectionManager;

import java.util.List;
import java.util.Set;

public class AlarmSendLogQueryTest {

    public void doExecute() throws Exception {
        List<AlarmSendLog> alarmSendLogs = new AlarmSendLogQuery()
                .setDBAccess(ApeConnectionManager.getInstance().getMasterDbAccess())
                .setParam("ids", Set.of(12690, 12689)).execute().asList();

        for (AlarmSendLog alarmSendLog : alarmSendLogs) {
            for (ApeAlarmDetail alarmDetail : alarmSendLog.getAlarm().getAlarmDetails()) {
                System.out.println(alarmSendLog.getId() + "\t" + alarmDetail.getAid() + "\t" + alarmDetail.getId());
            }

        }

    }
}
