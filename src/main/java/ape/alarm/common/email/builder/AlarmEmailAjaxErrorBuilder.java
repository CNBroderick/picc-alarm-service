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

public class AlarmEmailAjaxErrorBuilder extends AbstractAlarmEmailBuilder {

    private final static String SUMMARY_CONTENT = """
            ${company} ${ajax.portal}-${ajax.front}-${ajax.name} ${step.url} 调用的 ${url.name} - ${step.ajax} Ajax ${datetime} 出现Ajax错误异常，告警ID：${alarm.id}，告警详情如下：
            """;

    private final static String SUMMARY_RESTORED = """
            ${company} ${ajax.portal}-${ajax.front}-${ajax.name} ${step.url} 调用的 ${url.name} - ${step.ajax} Ajax ${datetime} 出现Ajax错误异常，告警ID：${alarm.id}，告警恢复时间：${restored.time}，告警详情如下：
            """;

    private final SchemaFactory schemaFactory = new SchemaFactory().string("终端IP", "操作员").datetimeField("时间").string("出错AjaxURL", "错误内容");

    public AlarmEmailAjaxErrorBuilder() {
        super("Ajax错误告警");
    }

    @Override
    public String createSummary(ApeAlarm alarm) {
        return new TemplateParameterWriter(SUMMARY_CONTENT).write(createAjaxSummaryParameter(alarm));
    }

    @Override
    public String createRestoredSummary(ApeAlarm alarm) {
        return new TemplateParameterWriter(SUMMARY_RESTORED).write(createAjaxSummaryParameter(alarm));
    }

    @Override
    public void createTable(ApeAlarm alarm, AlarmEmailRenderRecordset recordset) {
        recordset.addRecord("告警ID", new DigitalFormatter(alarm.getId()).toInteger());
        recordset.addRecord("分公司名称", "%s[%s]".formatted(alarm.getComCodeName(), alarm.getComCodeId()));
        recordset.addRecord("前台页面应用ID", "%s[%s]".formatted(alarm.getUrlAppName(), alarm.getUrlAppId()));
        recordset.addRecord("步骤页URL", alarm.getUrl());
        recordset.addRecord("被调用前台微服务ID", "%s[%s]".formatted(alarm.getAjaxAppName(), alarm.getAjaxAppId()));
        recordset.addRecord("被调用Ajax URL", alarm.getAjaxUrl());
        recordset.addRecord("Ajax错误总次数", new DigitalFormatter(alarm.getNumberData("m_cnt_error")).toFormatted() + " 次");
        recordset.addRecord("Ajax调用次数", new DigitalFormatter(alarm.getNumberData("m_cnt_total")).toInteger() + " 次");
        recordset.addRecord("占比", new DigitalFormatter(alarm.getNumberData("m_error_rate")).toFormatted() + " %");
        recordset.addRecord("告警阈值", new DigitalFormatter(alarm.getNumberData("m_alarm_sla")).toFormatted() + " %");
        recordset.addRecord("涉及终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_total")).toInteger() + " 台");
        recordset.addRecord("异常终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_error")).toInteger() + " 台");
    }

    @Override
    protected Recordset createDetailSet(Collection<ApeAlarmDetail> apeAlarmDetails) {
        Recordset recordset = new Recordset(schemaFactory.get());
        apeAlarmDetails.forEach(alarmDetail -> new RecordFactory(recordset)
                .set("终端IP", alarmDetail.getIp())
                .set("操作员", alarmDetail.getUserCode())
                .set("时间", alarmDetail.getAlarmTime())
                .set("出错AjaxURL", alarmDetail.getUrl())
                .set("错误内容", alarmDetail.getData("d_msg"))
        );
        return recordset;
    }
}
