package ape.alarm.common.email.scanner;

import org.bklab.quark.util.mysql.InflectWord;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApeAlarmRestoredScannerTest {

    public static void main(String[] args) {
        System.out.println(InflectWord.getInstance().pluralize("alarmSendLog"));
    }

    @BeforeEach
    void setUp() {
    }

    //    @Test
    void checkRestored(@Autowired ApeAlarmAidScheduledScanner aidScheduledScanner, @Autowired ApeAlarmRestoredScanner scanner) {
        aidScheduledScanner.execute();
//        scanner.checkRestored();
    }
}
