package ape.alarm.common.email;

import ape.alarm.entity.alarm.ApeAlarm;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.url.UrlFrontPageMapping;
import ape.master.service.common.ApeServiceProvider;
import ape.master.service.common.ApeUrlMappingService;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.util.function.Function;

public class AlarmTitleFactory implements Function<ApeAlarm, String> {

    private static AlarmTitleFactory instance;
    private ApeUrlMappingService urlMappingService;

    private AlarmTitleFactory() {
    }

    public static AlarmTitleFactory getInstance() {
        if (instance == null) instance = new AlarmTitleFactory();

        return instance;
    }

    public String createCompanyShortName(String companyName) {
        return companyName
                .replaceAll("分公司", "")
                .replaceAll("市", "")
                .replaceAll("省", "");
    }

    public String createAlarmEmailTitle(ApeAlarm alarm) {
        return createAlarmEmailTitle(alarm, false);
    }

    public String translateAlarmType(ApeAlarm alarm) {
        return alarm.getAlarmType() == null ? "其他告警" : switch (alarm.getAlarmType()) {
            case 页面加载时间 -> "页面加载慢告警";
            case URL响应时间 -> "URL响应慢告警";
            case AJAX性能 -> "Ajax响应慢告警";
            case JS错误 -> "JS错误告警";
            case AJAX错误 -> "Ajax状态码错误告警";
            default -> alarm.getAlarmType().name() + "告警";
        };
    }
    public String createAlarmEmailTitle(ApeAlarm alarm, boolean restored) {
        StringBuilder builder = new StringBuilder()
                .append('[').append(ParameterEnum.电子邮件.邮件服务器所属应用名称.getEntryValue("嵌码监控告警"))
                .append(restored ? "恢复" : "")
                .append("] ");

        builder.append(translateAlarmType(alarm));

        builder.append('_').append(createCompanyShortName(alarm.getComCodeName()))
                .append(ParameterEnum.电子邮件.邮件服务器所属环境名称.getEntryValue("测试"))
                .append('_').append(alarm.getUrlAppName())
                .append('_').append(getFrontUrlMapName(alarm))
        ;

        builder.append(switch (alarm.getAlarmType()) {
            case AJAX性能, AJAX错误 -> "调用的_" + getAjaxUrlMapName(alarm) + "步骤";
            default -> "";
        });

        String datetime = alarm.getStartTime().toLocalDate().equals(alarm.getEndTime().toLocalDate())
                          ? "%s %s-%s".formatted(LocalDateTimeFormatter.Short(alarm.getStartTime().toLocalDate()),
                LocalDateTimeFormatter.Short(alarm.getStartTime().toLocalTime()), LocalDateTimeFormatter.Short(alarm.getEndTime().toLocalTime()))
                          : "%s - %s".formatted(LocalDateTimeFormatter.Short(alarm.getStartTime()), LocalDateTimeFormatter.Short(alarm.getEndTime()));

        builder.append(" ").append(datetime).append(" #告警ID：").append(alarm.getId());

        if (restored && alarm.getRestoredTime() != null) {
            builder.append(" 告警恢复时间：").append(LocalDateTimeFormatter.Short(alarm.getRestoredTime()));
        }

        return builder.toString();
    }

    private String getFrontUrlMapName(ApeAlarm alarm) {
        return getUrlMappingService().getUrlMappingOptional(alarm.getUrlAppId(), alarm.getUrl())
                .map(UrlFrontPageMapping::getName).orElseGet(alarm::getUrl);
    }

    private String getAjaxUrlMapName(ApeAlarm alarm) {
        return getUrlMappingService().getUrlMappingOptional(alarm.getUrlAppId(), alarm.getAjaxUrl())
                .map(UrlFrontPageMapping::getName).orElseGet(alarm::getAjaxUrl);
    }

    public ApeUrlMappingService getUrlMappingService() {
        if (urlMappingService == null) {
            urlMappingService = ApeServiceProvider.getInstance().urlMappingService();
        }

        if (urlMappingService == null) {
            urlMappingService = ApeUrlMappingService.getInstance();
        }

        return urlMappingService;
    }

    @Override
    public String apply(ApeAlarm alarm) {
        return createAlarmEmailTitle(alarm);
    }
}
