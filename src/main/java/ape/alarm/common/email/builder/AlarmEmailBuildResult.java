package ape.alarm.common.email.builder;

import org.bklab.quark.util.json.GsonJsonObjectUtil;

import javax.mail.internet.MimeMultipart;

public class AlarmEmailBuildResult {

    private MimeMultipart mimeMultipart;
    private String attachmentName;
    private byte[] attachment;
    private String message;

    public MimeMultipart getMimeMultipart() {
        return mimeMultipart;
    }

    public AlarmEmailBuildResult setMimeMultipart(MimeMultipart mimeMultipart) {
        this.mimeMultipart = mimeMultipart;
        return this;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public AlarmEmailBuildResult setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public AlarmEmailBuildResult setAttachment(byte[] attachment) {
        this.attachment = attachment;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AlarmEmailBuildResult setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return new GsonJsonObjectUtil(this).pretty();
    }
}
