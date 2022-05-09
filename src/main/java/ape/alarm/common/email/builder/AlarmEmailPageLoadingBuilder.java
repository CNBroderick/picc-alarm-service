package ape.alarm.common.email.builder;

import ape.alarm.common.email.AlarmEmailRenderRecordset;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmDetail;
import dataq.core.data.schema.Recordset;
import org.bklab.quark.util.number.DigitalFormatter;
import org.bklab.quark.util.schema.RecordFactory;
import org.bklab.quark.util.schema.SchemaFactory;
import org.bklab.quark.util.text.TemplateParameterWriter;

import java.util.Collection;

public class AlarmEmailPageLoadingBuilder extends AbstractAlarmEmailBuilder {

    private final static String SUMMARY_CONTENT = """
            ${company} ${app.portal}-${app.front}-${app.name} ${step} 步骤 ${datetime} 出现页面加载时间异常，告警ID：${alarm.id}，告警详情如下：
            """;

    private final static String SUMMARY_RESTORED = """
            ${company} ${app.portal}-${app.front}-${app.name} ${step} 步骤 ${datetime} 出现页面加载时间异常，告警ID：${alarm.id}，告警恢复时间：${restored.time}，告警详情如下：
            """;

    private final SchemaFactory schemaFactory = new SchemaFactory().string("终端IP", "操作员").datetimeField("时间").string("关键页面", "页面加载时间");


    public AlarmEmailPageLoadingBuilder() {
        super("页面加载性能告警");
    }

    @Override
    public String createSummary(ApeAlarm alarm) {
        return new TemplateParameterWriter(SUMMARY_CONTENT).write(createUrlSummaryParameter(alarm));
    }

    @Override
    public String createRestoredSummary(ApeAlarm alarm) {
        return new TemplateParameterWriter(SUMMARY_RESTORED).write(createUrlSummaryParameter(alarm));
    }

    @Override
    public void createTable(ApeAlarm alarm, AlarmEmailRenderRecordset recordset) {
        recordset.addRecord("告警ID", new DigitalFormatter(alarm.getId()).toInteger());
        recordset.addRecord("分公司名称", "%s[%s]".formatted(alarm.getComCodeName(), alarm.getComCodeId()));
        recordset.addRecord("前台页面应用ID", "%s[%s]".formatted(alarm.getUrlAppName(), alarm.getUrlAppId()));
        recordset.addRecord("步骤页url", alarm.getUrl());
        recordset.addRecord("页面加载时间标准阈值", new DigitalFormatter(alarm.getNumberData("m_sla")).toFormatted() + " ms");
        recordset.addRecord("超过标准阈值次数", new DigitalFormatter(alarm.getNumberData("m_cnt_slow")).toInteger() + " 次");
        recordset.addRecord("页面调用次数", new DigitalFormatter(alarm.getNumberData("m_cnt_total")).toInteger() + " 次");
        recordset.addRecord("异常占比", new DigitalFormatter(alarm.getNumberData("m_slow_rate")).toFormatted() + " %");
        recordset.addRecord("告警阈值", new DigitalFormatter(alarm.getNumberData("m_alarm_sla")).toFormatted() + " %");
        recordset.addRecord("异常幅度", new DigitalFormatter(alarm.getNumberData("m_cnt_ab")).toFormatted());
        recordset.addRecord("涉及终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_total")).toInteger() + " 台");
        recordset.addRecord("异常终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_slow")).toInteger() + " 台");
    }

    @Override
    protected Recordset createDetailSet(Collection<ApeAlarmDetail> apeAlarmDetails) {
        Recordset recordset = new Recordset(schemaFactory.get());
        apeAlarmDetails.forEach(alarmDetail -> new RecordFactory(recordset)
                .set("终端IP", alarmDetail.getIp())
                .set("操作员", alarmDetail.getUserCode())
                .set("时间", alarmDetail.getAlarmTime())
                .set("关键页面", alarmDetail.getUrl())
                .set("页面加载时间", new DigitalFormatter(alarmDetail.getNumberData("m_slow_time")).toFormatted() + " ms")
        );
        return recordset;
    }
}
