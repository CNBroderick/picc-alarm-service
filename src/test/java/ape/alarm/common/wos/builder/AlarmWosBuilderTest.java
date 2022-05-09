package ape.alarm.common.wos.builder;

import ape.Application;
import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.entity.common.ApeApplicationEnum;
import ape.master.security.SecurityUtils;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlarmWosBuilderTest {
    @Resource
    private ConfigurableApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        SecurityUtils.setCurrentApplication(ApeApplicationEnum.告警后台服务);
    }

    @Test
    public void test() {
        Application.setApplicationContext(applicationContext);
        ApeAlarm alarm = new ApeOperationBuilder().add("id", 10083)
                .<ApeAlarm>executeQueryList(ApeAlarmOperationEnum.ApeAlarmQuery).get(0);
        System.out.println(new GsonJsonObjectUtil(new AlarmWosBuilder(alarm).build().createPostJson()).pretty());
    }

}
