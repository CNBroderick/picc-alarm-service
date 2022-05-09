package ape.alarm.common.email.sender;

import ape.alarm.common.jdbc.ApeOperationBuilder;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.email.EmailMimeMessageBuilder;
import ape.master.common.parameter.ApeParameter;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class AlarmEmailSender {

    private final AlarmSendLog alarmSendLog;

    public AlarmEmailSender(AlarmSendLog alarmSendLog) {
        this.alarmSendLog = alarmSendLog;
    }

    public boolean send() {
        try {
            MimeMessage mimeMessage = createMimeMessage();
            if (mimeMessage == null) return false;

            MimeMultipart multipart = createMimeMultipart();
            if (multipart == null) return false;

            mimeMessage.setSubject(alarmSendLog.getTitle());
            mimeMessage.setSentDate(new Date());

            Address[] addresses = createAddress();
            if (addresses == null) return false;

            mimeMessage.setRecipients(Message.RecipientType.TO, addresses);
            mimeMessage.setContent(multipart);

            updateStatus(alarmSendLog.setSendingStatus());

            mimeMessage.saveChanges();
            Transport.send(mimeMessage);
            updateStatus(alarmSendLog.setSuccessSendStatus().clearError());

            return true;
        } catch (Exception exception) {
            LoggerFactory.getLogger(getClass()).error("投递到邮件服务器失败。\n" + alarmSendLog, exception);
            alarmSendLog.setFailedSendStatus().setErrorStack(exception).setErrorMessage("投递到邮件服务器失败。");
            updateStatus(alarmSendLog);
            return false;
        }
    }

    private MimeMessage createMimeMessage() {
        try {
            return new EmailMimeMessageBuilder(ApeParameter.getInstance()).createMimeMessage();
        } catch (Exception exception) {
            LoggerFactory.getLogger(getClass()).error("初始化MimeMessage失败。\n" + alarmSendLog, exception);
            alarmSendLog.setFailedSendStatus().setErrorStack(exception).setErrorMessage("初始化MimeMessage失败。");
            update(alarmSendLog);
            return null;
        }
    }

    private Address[] createAddress() {
        try {
            return new Address[]{new InternetAddress(alarmSendLog.getAddress(), alarmSendLog.getContactName())};
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("邮件地址[" + alarmSendLog.getAddress() + "]错误。\n" + alarmSendLog, e);
            alarmSendLog.setFailedSendStatus().setErrorStack(e).setErrorMessage("邮件地址错误。");
            update(alarmSendLog);
        }
        return null;
    }

    private MimeMultipart createMimeMultipart() {
        try {

            MimeMultipart multipart = new MimeMultipart("mixed");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContentLanguage(new String[]{"zh-cn"});
            textPart.setHeader("Content-Type", "text/html; charset=UTF-8");
            textPart.setContent(alarmSendLog.getMessage(), "text/html; charset=UTF-8");
            multipart.addBodyPart(textPart);

            MimeBodyPart filePart = new MimeBodyPart();
            filePart.setFileName(MimeUtility.encodeText(alarmSendLog.getAttachmentName(), StandardCharsets.UTF_8.name(), null));
            filePart.setDataHandler(new DataHandler(new ByteArrayDataSource(new ByteArrayInputStream(alarmSendLog.getAttachment()), "application/msexcel")));
            multipart.addBodyPart(filePart);

            return multipart;
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("创建邮件内容失败。\n" + alarmSendLog, e);
            alarmSendLog.setFailedSendStatus().setErrorStack(e).setErrorMessage("创建邮件内容失败。");
            update(alarmSendLog);
        }
        return null;
    }

    public void update(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogUpdate);
    }

    public void updateStatus(AlarmSendLog alarmSendLog) {
        new ApeOperationBuilder().add("alarmSendLog", alarmSendLog).execute(ApeAlarmOperationEnum.AlarmSendLogUpdateStatus);
    }

    public AlarmSendLog getAlarmSendLog() {
        return alarmSendLog;
    }
}
