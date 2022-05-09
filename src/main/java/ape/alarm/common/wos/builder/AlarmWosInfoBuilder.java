package ape.alarm.common.wos.builder;

import ape.alarm.common.core.AlarmContentRenderManger;
import ape.alarm.common.email.AlarmEmailRenderRecordset;
import ape.alarm.common.email.AlarmTitleFactory;
import ape.alarm.common.email.builder.AbstractAlarmEmailBuilder;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmDetail;
import ape.alarm.entity.po.AlarmWosInfo;
import ape.master.entity.code.AppCode;
import org.bklab.quark.util.text.StringExtractor;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AlarmWosInfoBuilder {

    private final ApeAlarm alarm;
    private final Set<ApeAlarmDetail> alarmDetails;
    private AlarmWosInfo alarmWosInfo;

    public AlarmWosInfoBuilder(ApeAlarm alarm, Set<ApeAlarmDetail> alarmDetails) {
        this.alarm = alarm;
        this.alarmDetails = alarmDetails;
    }

    private BigDecimal getEffectRate() {
        return BigDecimal.valueOf((switch (alarm.getAlarmType()) {
            case URL响应时间, 页面加载时间, AJAX性能 -> alarm.getNumberData("m_slow_rate");
            case JS错误, AJAX错误 -> alarm.getNumberData("m_error_rate");
            default -> 0d;
        }).doubleValue());
    }

    private BigDecimal getEffectAmplitude() {
        return switch (alarm.getAlarmType()) {
            case URL响应时间, 页面加载时间, AJAX性能 -> BigDecimal.valueOf(alarm.getNumberData("m_cnt_ab").doubleValue());
            default -> BigDecimal.ZERO;
        };
    }

    private String createTitle() {
        return AlarmTitleFactory.getInstance().createAlarmEmailTitle(alarm);
    }

    private String createDescription() {
        AbstractAlarmEmailBuilder builder = AlarmContentRenderManger.getInstance().get(alarm.getAlarmType());

        AlarmEmailRenderRecordset renderRecordset = new AlarmEmailRenderRecordset();
        builder.createTable(alarm, renderRecordset);
        return builder.createSummary(alarm) + "\n\n" + Arrays.stream(renderRecordset.getRecords()).map(record ->
                record.getString(AlarmEmailRenderRecordset.FIELD_NAME) + "：" + record.getString(AlarmEmailRenderRecordset.FIELD_VALUE)
        ).collect(Collectors.joining("\n"));
    }

    private String createEffectType() {
        String slowRate = Optional.ofNullable(alarm.getData("m_slow_rate"))
                .orElseGet(() -> alarm.getData("m_error_rate"));
        double rate = StringExtractor.parseDouble(slowRate);
        if (rate > 50) return "2";
        if (rate > 30) return "7";
        if (rate > 0) return "3";
        return "6";
    }

    private String createIpAddress() {
        AtomicInteger size = new AtomicInteger();
        Set<String> b = alarmDetails.stream().map(ApeAlarmDetail::getIp).distinct().collect(Collectors.toSet());
        Set<String> a = b.stream().takeWhile(ip ->
                size.addAndGet(ip.length() + 2) < 253
        ).collect(Collectors.toSet());
        return String.join(", ", a) + (a.size() < b.size() ? "..." : "");
    }

    public AlarmWosInfoBuilder build(int wosId) {
        String effectSystem = Optional.ofNullable(alarm.getAjaxApp()).map(AppCode::getWosId).orElseGet(() ->
                Optional.ofNullable(alarm.getUrlApp()).map(AppCode::getWosId).orElse(null));
        if (effectSystem == null) return this;

        this.alarmWosInfo = new AlarmWosInfo()
                .setWosId(wosId)
                .setAlarmId(alarm.getId())
                .setAffectSystem(effectSystem)
                .setErrorSystem(effectSystem)
                .setPlaceTime(alarm.getAlarmTime())
                .setAffectRate(getEffectRate())
                .setAffectAmplitude(getEffectAmplitude())
                .setTitle(createTitle())
                .setDescription(createDescription())
                .setAffectType(createEffectType())
                .setIpAddress(createIpAddress())
                .setAffectArea(alarm.getComCode().getWosId())
                .setRecoveryTime(null)
                .setFaultDuration(null)
                .setImpactFunction(null)
                .setSendTime(null)
        ;
        return this;
    }

    public AlarmWosInfo getAlarmWosInfo() {
        return alarmWosInfo;
    }
}
