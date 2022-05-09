package ape.alarm.common.email.builder;

import ape.alarm.common.email.AlarmEmailRenderRecordset;
import ape.alarm.common.email.AlarmExcelAttachmentBuilder;
import ape.alarm.common.email.render.AlarmEmailRender;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.alarm.ApeAlarmDetail;
import ape.master.common.parameter.ParameterEnum;
import ape.master.entity.url.UrlFrontPageMapping;
import ape.master.service.common.ApeServiceProvider;
import com.google.gson.JsonObject;
import dataq.core.data.schema.Recordset;
import org.bklab.quark.util.number.DigitalFormatter;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

public abstract class AbstractAlarmEmailBuilder {

    private final String alarmType;

    protected AbstractAlarmEmailBuilder(String alarmType) {
        this.alarmType = alarmType;
    }

    abstract public String createSummary(ApeAlarm alarm);

    abstract public String createRestoredSummary(ApeAlarm alarm);

    abstract public void createTable(ApeAlarm alarm, AlarmEmailRenderRecordset recordset);

    abstract protected Recordset createDetailSet(Collection<ApeAlarmDetail> apeAlarmDetails);

    public AlarmEmailBuildResult build(ApeAlarm alarm, String recipientName) throws Exception {
        return build(alarm, recipientName, false);
    }

    public AlarmEmailBuildResult build(ApeAlarm alarm, String recipientName, boolean restored) throws Exception {
        AlarmEmailRender render = new AlarmEmailRender()
                .setAlarmSummary(restored ? createRestoredSummary(alarm) : createSummary(alarm))
                .setRecipientName(recipientName)
                .addData(recordset -> createTable(alarm, recordset));

        byte[] byteArray = new AlarmExcelAttachmentBuilder("页面加载性能告警")
                .createSheet("汇总", render.getRecordset())
                .createSheet("明细", createDetailSet(alarm.getAlarmDetails()))
                .createDownloadStream().toByteArray();

        MimeMultipart multipart = new MimeMultipart("mixed");
        MimeBodyPart textPart = new MimeBodyPart();
        String message = render.body();
        textPart.setContentLanguage(new String[]{"zh-cn"});
        textPart.setHeader("Content-Type", "text/html; charset=UTF-8");
        textPart.setContent(message, "text/html; charset=UTF-8");
        multipart.addBodyPart(textPart);

        String attachmentName = "%s-%s-%s.xlsx".formatted(ParameterEnum.电子邮件.邮件服务器所属应用名称.getEntryValue("嵌码监控告警"),
                alarmType, DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss").format(LocalDateTime.now()));
        MimeBodyPart filePart = new MimeBodyPart();
        filePart.setFileName(MimeUtility.encodeText(attachmentName, StandardCharsets.UTF_8.name(), null));
        filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(new ByteArrayInputStream(byteArray), "application/msexcel")));
        multipart.addBodyPart(filePart);

        return new AlarmEmailBuildResult().setAttachment(byteArray).setAttachmentName(attachmentName).setMessage(message).setMimeMultipart(multipart);
    }

    protected String getPageUrlName(String appcode, String url) {
        return ApeServiceProvider.getInstance().urlMappingService()
                .getUrlMappingOptional(appcode, url).map(UrlFrontPageMapping::getName).orElse(url);
    }

    private JsonObject createSummaryJsonObject(ApeAlarm alarm) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("company", alarm.getComCodeName());
        jsonObject.addProperty("restored.time", LocalDateTimeFormatter.Short(alarm.getRestoredTime()));
        jsonObject.addProperty("datetime",
                alarm.getStartTime().toLocalDate().equals(alarm.getEndTime().toLocalDate())
                ? "%s %s-%s".formatted(LocalDateTimeFormatter.Short(alarm.getStartTime().toLocalDate()),
                        LocalDateTimeFormatter.Short(alarm.getStartTime().toLocalTime()), LocalDateTimeFormatter.Short(alarm.getEndTime().toLocalTime()))
                : "%s - %s".formatted(LocalDateTimeFormatter.Short(alarm.getStartTime()), LocalDateTimeFormatter.Short(alarm.getEndTime())));
        jsonObject.addProperty("alarm.id", new DigitalFormatter(alarm.getId()).toInteger());

        return jsonObject;
    }

    protected JsonObject createAjaxSummaryParameter(ApeAlarm alarm) {
        JsonObject jsonObject = createSummaryJsonObject(alarm);

        jsonObject.addProperty("ajax.portal", alarm.getAjaxAppPortal());
        jsonObject.addProperty("ajax.front", alarm.getAjaxAppFront());
        jsonObject.addProperty("ajax.name", alarm.getAjaxAppName());
        jsonObject.addProperty("step.url", getPageUrlName(alarm.getUrlAppId(), alarm.getUrl()));
        jsonObject.addProperty("url.name", alarm.getUrlAppName());
        jsonObject.addProperty("step.ajax", getPageUrlName(alarm.getUrlAppId(), alarm.getAjaxUrl()));

        return jsonObject;
    }

    protected JsonObject createUrlSummaryParameter(ApeAlarm alarm) {
        JsonObject jsonObject = createSummaryJsonObject(alarm);

        jsonObject.addProperty("app.portal", alarm.getUrlAppPortal());
        jsonObject.addProperty("app.front", alarm.getUrlAppFront());
        jsonObject.addProperty("app.name", alarm.getUrlAppName());
        jsonObject.addProperty("step", getPageUrlName(alarm.getUrlAppId(), alarm.getUrl()));

        return jsonObject;
    }

    public String getAlarmType() {
        return alarmType;
    }
}
