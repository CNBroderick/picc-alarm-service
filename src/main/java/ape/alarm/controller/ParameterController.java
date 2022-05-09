package ape.alarm.controller;

import ape.alarm.entity.alarm.AlarmDataFieldTranslator;
import ape.master.common.parameter.ApeParameter;
import ape.master.common.parameter.IParameterEnum;
import ape.master.common.parameter.ParameterEnum;
import com.google.gson.JsonObject;
import org.bklab.quark.util.time.LocalDateTimeFormatter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parameter")
public class ParameterController {

    @RequestMapping("/reload")
    public String reload() {
        ApeParameter.getInstance().forceReload();
        AlarmDataFieldTranslator.getInstance().reload();
        return "ok";
    }

    @RequestMapping("/reload-time")
    public String getReloadTime() {
        return LocalDateTimeFormatter.Short(ApeParameter.getInstance().getReloadTime());
    }

    @RequestMapping("/value")
    public String value(@RequestParam String name) {
        return name + " = " + ApeParameter.getInstance().getEntryValue(name, "");
    }

    @RequestMapping("/list")
    public String list() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("后台接口", getValue(ParameterEnum.后台接口.values()));
        jsonObject.add("告警系统", getValue(ParameterEnum.告警系统.values()));
        jsonObject.add("应用导航", getValue(ParameterEnum.应用导航.values()));
        jsonObject.add("操作日志", getValue(ParameterEnum.操作日志.values()));
        jsonObject.add("电子邮件", getValue(ParameterEnum.电子邮件.values()));
        jsonObject.add("系统参数", getValue(ParameterEnum.系统参数.values()));
        return jsonObject.toString();
    }

    private JsonObject getValue(IParameterEnum[] parameterEnum) {
        ApeParameter instance = ApeParameter.getInstance();
        JsonObject jsonObject = new JsonObject();
        for (IParameterEnum iParameterEnum : parameterEnum) {
            jsonObject.addProperty(iParameterEnum.getName(), instance.getEntryValue(iParameterEnum.getName(), ""));
        }
        return jsonObject;
    }

}
