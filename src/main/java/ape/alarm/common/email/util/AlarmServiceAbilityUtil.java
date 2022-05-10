package ape.alarm.common.email.util;

import ape.master.common.parameter.IParameterEnum;
import ape.master.common.parameter.ParameterEnum;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;

@Slf4j
public class AlarmServiceAbilityUtil {

    private static final IParameterEnum global = ParameterEnum.后台服务.启用告警后台服务;
    private final IParameterEnum ability;
    private final IParameterEnum lastRunTime;

    public AlarmServiceAbilityUtil(IParameterEnum ability, IParameterEnum lastRunTime) {
        this.ability = ability;
        this.lastRunTime = lastRunTime;
    }

    public boolean executable() {
        try {
            return global.getEntryValue(true) && ability.getEntryValue(true);
        } catch (Exception e) {
            log.error("获取参数[" + ability.getName() + "]失败，放行执行。", e);
            return true;
        }
    }

    public void writeRunTime() {
        try {
            lastRunTime.setValue(LocalDateTimeFormatter.Short(LocalDateTime.now()));
        } catch (Exception e) {
            log.error("写入参数[" + lastRunTime.getName() + "]失败。", e);
        }
    }

    public String getRunTime() {
        try {
            return lastRunTime.getEntryValue((String) null);
        } catch (Exception e) {
            log.error("读取参数[" + lastRunTime.getName() + "]失败。", e);
        }
        return null;
    }

    public void writeRunning() {
        try {
            lastRunTime.setValue("正在运行[自" + LocalDateTimeFormatter.Short(LocalDateTime.now()) + "开始]");
        } catch (Exception e) {
            log.error("写入参数[" + lastRunTime.getName() + "]失败。", e);
        }
    }
}
