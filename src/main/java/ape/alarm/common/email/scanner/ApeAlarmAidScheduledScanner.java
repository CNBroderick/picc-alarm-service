package ape.alarm.common.email.scanner;


import ape.alarm.common.email.util.AlarmServiceAbilityUtil;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.master.common.parameter.ParameterEnum;
import ape.master.operation.ApeConnectionManager;
import io.prometheus.client.Counter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.entity.dao.PreparedStatementHelper;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.util.List;

@Component
@Configurable
@EnableScheduling
@Getter
@Slf4j
public class ApeAlarmAidScheduledScanner {

    private static final String SQL_UPDATE_ALARM_DETAIL = """
            UPDATE `tb_alarm_detail` AS `d` SET `d_aid` = (SELECT `a`.`d_id` FROM `tb_alarm` AS `a` WHERE `a`.`d_alarm_id` = `d`.`d_alarm_id` )
             WHERE `d`.`d_alarm_time` >= DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 5 MINUTE) AND `d`.`d_aid` IS NULL;
            """;
    private static final String SQL_UPDATE_ALARM_SEND_LOG = """
            UPDATE `tb_alarm_send_log` AS `d` SET `d_aid` = (SELECT `a`.`d_id` FROM `tb_alarm` AS `a` WHERE `a`.`d_alarm_id` = `d`.`d_alarm_id` )
             WHERE `d`.`d_create_time` >= DATE_SUB(CURRENT_TIMESTAMP(), INTERVAL 5 MINUTE) AND `d`.`d_aid` IS NULL;
            """;
    private final static AlarmServiceAbilityUtil ABILITY_UTIL = new AlarmServiceAbilityUtil(
            ParameterEnum.后台服务.启用告警表aid同步后台服务, ParameterEnum.后台服务.告警表aid同步后台服务最后运行时间
    );
    public static boolean IS_RUNNING = false;
    private static List<AlarmSendLog> currentSend;
    private final Counter alarmDetailCounter;
    private final Counter sendLogCounter;

    public ApeAlarmAidScheduledScanner() {
        alarmDetailCounter = Counter.build("update_alarm_detail_aid_count", "更新`tb_alarm_detail`表`d_Aid`").register();
        sendLogCounter = Counter.build("update_alarm_send_log_aid_count", "更新`tb_alarm_send_log`表`d_Aid`").register();
    }

    @Transactional
    @Scheduled(cron = "40 0/5 * * * ? ")
    public void execute() {
        if (IS_RUNNING) {
            log.debug("处理AlarmAid服务尚未完成，停止本次运行。");
            return;
        }

        if (!ABILITY_UTIL.executable()) return;

        log.debug("处理AlarmAid服务：开始运行。");
        IS_RUNNING = Boolean.TRUE;
        try {
            ABILITY_UTIL.writeRunning();
            Connection master = ApeConnectionManager.getInstance().getMaster();

            int modifiedDetail = new PreparedStatementHelper(master, SQL_UPDATE_ALARM_DETAIL).executeUpdate();
            int modifiedSendLog = new PreparedStatementHelper(master, SQL_UPDATE_ALARM_SEND_LOG).executeUpdate();
            master.close();

            if (modifiedDetail + modifiedSendLog > 0) {
                log.info("处理AlarmAid服务：更新`tb_alarm_detail`表%d行数据、更新`tb_alarm_send_log`表%d行数据。".formatted(modifiedDetail, modifiedSendLog));
                alarmDetailCounter.inc(modifiedDetail);
                sendLogCounter.inc(modifiedSendLog);
            }
        } catch (Exception e) {
            log.error("处理AlarmAid服务失败。", e);
        } finally {
            IS_RUNNING = Boolean.FALSE;
            ABILITY_UTIL.writeRunTime();
            log.debug("处理AlarmAid服务：结束运行。");
        }
    }

}
