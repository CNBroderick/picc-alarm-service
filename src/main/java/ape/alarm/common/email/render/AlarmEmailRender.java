package ape.alarm.common.email.render;

import ape.alarm.common.email.AlarmEmailRenderRecordset;
import ape.master.common.email.IEmailContentRender;
import com.google.gson.JsonObject;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.Recordset;
import org.bklab.quark.util.schema.RecordFactory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AlarmEmailRender implements IEmailContentRender {

    public final static String FIELD_NAME = AlarmEmailRenderRecordset.FIELD_NAME;
    public final static String FIELD_DESCRIPTION = AlarmEmailRenderRecordset.FIELD_DESCRIPTION;
    public final static String FIELD_VALUE = AlarmEmailRenderRecordset.FIELD_VALUE;

    private final AlarmEmailRenderRecordset recordset = new AlarmEmailRenderRecordset();
    private String recipientName;
    private String alarmSummary;


    public AlarmEmailRender() {
    }

    public AlarmEmailRender(String recipientName, String alarmSummary) {
        this.recipientName = recipientName;
        this.alarmSummary = alarmSummary;
    }

    public Recordset getRecordset() {
        return recordset;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public AlarmEmailRender setRecipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public String getAlarmSummary() {
        return alarmSummary;
    }

    public AlarmEmailRender setAlarmSummary(String alarmSummary) {
        this.alarmSummary = alarmSummary;
        return this;
    }

    public AlarmEmailRender addData(String name, String value) {
        new RecordFactory(recordset.createRecord()).set(FIELD_NAME, name).set(FIELD_DESCRIPTION, "").set(FIELD_VALUE, value);
        return this;
    }

    public AlarmEmailRender addData(String name, String value, String description) {
        new RecordFactory(recordset.createRecord()).set(FIELD_NAME, name).set(FIELD_DESCRIPTION, description).set(FIELD_VALUE, value);
        return this;
    }

    public AlarmEmailRender addData(Record record) {
        recordset.addRecord(record);
        return this;
    }

    public AlarmEmailRender addData(Consumer<AlarmEmailRenderRecordset> consumer) {
        consumer.accept(recordset);
        return this;
    }

    public String body() {
        JsonObject data = new JsonObject();
        data.addProperty("recipient.name", recipientName);
        data.addProperty("alarm.summary", alarmSummary);
        data.addProperty("table.head", createTableHead(FIELD_NAME, FIELD_VALUE));
        data.addProperty("table.body", createTableBody(recordset.asList()));
        return createBody(data);
    }

    @Override
    public String getTemplate() {
        return """
                <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
                <html xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
                                
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
                    <title>${EMAIL_TITLE}</title>
                </head>
                                
                <body style='margin: 0;'>
                                
                <table width="100%" style="background-color:#fff;margin:0 auto;" cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <table width="100%" style="background-color:#fff;margin:0 auto;" cellpadding="0" cellspacing="0">
                                <tbody>
                                <!-- 主体内容 -->
                                <tr>
                                    <td width="40" style=""> </td>
                                    <td width="520" style="line-height:20px;">
                                        <p style="margin:0;padding:0;text-align:left;font-size:20px;line-height:24px;margin-bottom:1em">
                                            <span style="color:#373d41">尊敬的</span>
                                            <span style="color:#ff8a00;">${recipient.name}</span>
                                            <span style="color:#373d41;">：</span>
                                            <br/>
                                        </p>
                                        <p style="margin:0;padding:0;text-align:left;font-size:20px;line-height:24px;">
                                            <span style="color:#373d41;margin-left:2em;">${alarm.summary}</span>
                                        </p>
                                
                                        <p style="margin:0;padding:0; ">&nbsp;</p>
                                
                                        <table border="1 " cellpadding="0 " cellspacing="0 "
                                               style="min-width:520px; border-color: #E0E6E9;border-style:solid; border-collapse: collapse; ">
                                            <tbody>
                                            ${table.head}
                                            ${table.body}
                                            </tbody>
                                        </table>
                                    </td>
                                    <td width="40 "> </td>
                                </tr>
                                <!-- 主体内容 END -->
                                <tr>
                                    <td colspan="3 " style="height:40px;line-height:40px; "> </td>
                                </tr>
                                </tbody>
                            </table>
                                
                                
                        </td>
                    </tr>
                </table>
                </body>
                </html>
                             
                """;
    }

    private String createTableHead(String... heads) {
        return Arrays.stream(heads)
                .map(field -> """
                                      <th style="background-color:#eaeef0; box-sizing: border-box; padding:0 20px; border-color: #E0E6E9; height:39px; text-align: center; vertical-align:middle; min-width:200px ">
                                      """ + field + "</th>"
                ).collect(Collectors.joining("", """
                        <tr style="color:#777f84;line-height:40px;">
                        """, "</tr>"));
    }

    private String createTableBody(List<Record> records) {
        return records.stream().map(record -> """
                <tr>
                    <td style="padding:10px 20px; box-sizing: border-box; text-align:center;border-color: #E0E6E9; vertical-align:middle ">
                        <p style="margin:0;padding:0;color:#373d41; ">%s</p>
                        <p style="margin:0;padding:0;color:#777f84 ">%s</p>
                    </td>
                    <td style="padding:10px 20px;box-sizing: border-box; border-color: #E0E6E9; text-align:center; vertical-align:middle ">
                        <p style="margin:0;padding:0;color:#ff8a00; ">%s</p>
                    </td>
                </tr>
                """.formatted(record.getString(FIELD_NAME), record.getString(FIELD_DESCRIPTION), record.getString(FIELD_VALUE))
        ).collect(Collectors.joining("\n"));
    }
}
