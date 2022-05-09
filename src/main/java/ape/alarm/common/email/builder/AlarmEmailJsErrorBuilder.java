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

public class AlarmEmailJsErrorBuilder extends AbstractAlarmEmailBuilder {

    private final static String SUMMARY_CONTENT = """
            ${company} ${app.portal}-${app.front}-${app.name} ${step} 步骤 ${datetime} 出现JS错误异常，告警ID：${alarm.id}，告警详情如下：
            """;

    private final static String SUMMARY_RESTORED = """
            ${company} ${app.portal}-${app.front}-${app.name} ${step} 步骤 ${datetime} 出现JS错误异常，告警ID：${alarm.id}，告警恢复时间：${restored.time}，告警详情如下：
            """;

    private final SchemaFactory schemaFactory = new SchemaFactory().string("终端IP", "操作员").datetimeField("时间")
            .string("出错页面URL", "JS错误内容", "出错文件名").intField("错误所在行号", "错误所在列号");


    public AlarmEmailJsErrorBuilder() {
        super("JS错误告警");
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
        recordset.addRecord("JavaScript错误次数", new DigitalFormatter(alarm.getNumberData("m_cnt_error")).toFormatted() + " 次");
        recordset.addRecord("页面调用次数", new DigitalFormatter(alarm.getNumberData("m_cnt_total")).toInteger() + " 次");
        recordset.addRecord("占比", new DigitalFormatter(alarm.getNumberData("m_error_rate")).toFormatted() + " %");
        recordset.addRecord("告警阈值", new DigitalFormatter(alarm.getNumberData("m_alarm_sla")).toFormatted() + " %");
        recordset.addRecord("终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_total")).toInteger() + " 台");
        recordset.addRecord("异常终端数量", new DigitalFormatter(alarm.getNumberData("m_ip_error")).toInteger() + " 台");
    }

    @Override
    protected Recordset createDetailSet(Collection<ApeAlarmDetail> apeAlarmDetails) {
        Recordset recordset = new Recordset(schemaFactory.get());
        apeAlarmDetails.forEach(alarmDetail -> new RecordFactory(recordset)
                .set("终端IP", alarmDetail.getIp())
                .set("操作员", alarmDetail.getUserCode())
                .set("时间", alarmDetail.getAlarmTime())
                .set("出错页面URL", alarmDetail.getUrl())
                .set("JS错误内容", alarmDetail.getData("d_msg"))
                .set("出错文件名", alarmDetail.getData("d_filename"))
                .set("错误所在行号", new DigitalFormatter(alarmDetail.getNumberData("d_lineno")).toInteger() + " 行")
                .set("错误所在行号", new DigitalFormatter(alarmDetail.getNumberData("d_colno")).toInteger() + " 列")
        );
        return recordset;
    }
}
