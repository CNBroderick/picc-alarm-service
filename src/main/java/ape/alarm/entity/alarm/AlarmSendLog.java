package ape.alarm.entity.alarm;

import ape.master.entity.alarm.po.AlarmSendLogPO;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.entity.alarm.transmission.AlarmContactChannelTypeEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationBinding;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import ape.master.entity.user.User;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bklab.quark.util.json.GsonJsonObjectUtil;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class AlarmSendLog {

    /**
     * 发送日志id
     */
    private int id = -1;
    /**
     * 告警聚合唯一id
     */
    private int aid;
    /**
     * 告警聚合id
     */
    private String alarmId;
    private ApeAlarm alarm;
    /**
     * 告警策略id
     */
    private int policyId = -1;
    @Expose
    transient private AlarmNotificationPolicy policy;
    /**
     * 发送渠道
     */
    private AlarmContactChannelTypeEnum channel;
    /**
     * 告警联系人id
     */
    private String account;
    @Expose
    transient private AlarmNotificationBinding binding;
    /**
     * 发送地址
     */
    private String address;
    /**
     * 发送告警标题
     */
    private String title;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 发送时间
     */
    private LocalDateTime sendTime;
    /**
     * 发送状态。发送中、发送成功、发送失败。
     */
    private AlarmSendStatus status = AlarmSendStatus.待发送;
    /**
     * 告警发送内容
     */
    @Expose
    transient private String message;

    /**
     * 附件标题
     */
    private String attachmentName;
    /**
     * 附件
     */
    @Expose
    transient private byte[] attachment;
    /**
     * 其他数据
     */
    private JsonObject data = new JsonObject();

    /**
     * 异常摘要
     */
    private String errorMessage;

    /**
     * 异常堆栈
     */
    private String errorStack;

    private boolean resend = false;

    public AlarmSendLog() {

    }

    public int getParentAlarmId() {
        return alarm == null ? 0 : alarm.getId();
    }

    public String getDataJson() {
        return data == null ? "{}" : data.toString();
    }

    public int getId() {
        return id;
    }

    public AlarmSendLog setId(int id) {
        this.id = id;
        return this;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public AlarmSendLog setAlarmId(String alarmId) {
        this.alarmId = alarmId;
        return this;
    }

    public ApeAlarm getAlarm() {
        if (alarm != null && alarm.getId() == 0) alarm.setAlarmId(alarmId);
        return alarm;
    }

    public AlarmSendLog setAlarm(ApeAlarm alarm) {
        this.alarm = alarm;
        if (alarm != null && aid == 0) this.aid = alarm.getId();
        if (alarm != null && alarmId == null) this.alarmId = alarm.getAlarmId();
        return this;
    }

    public int getPolicyId() {
        return policyId;
    }

    public AlarmSendLog setPolicyId(int policyId) {
        this.policyId = policyId;
        return this;
    }

    public String getPolicyName() {
        return policy == null ? "" + id : policy.getName();
    }

    public AlarmNotificationPolicy getPolicy() {
        return policy;
    }

    public AlarmSendLog setPolicy(AlarmNotificationPolicy policy) {
        this.policy = policy;
        if (policy != null && policyId < 0) this.policyId = policy.getId();
        return this;
    }

    public AlarmContactChannelTypeEnum getChannel() {
        return channel;
    }

    public AlarmSendLog setChannel(String channel) {
        return setChannel(AlarmContactChannelTypeEnum.parse(channel));
    }

    public AlarmSendLog setChannel(AlarmContactChannelTypeEnum channel) {
        this.channel = channel;
        return this;
    }

    public String getChannelName() {
        return channel == null ? null : channel.name();
    }

    public String getAccount() {
        return account;
    }

    public AlarmSendLog setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getContactName() {
        return binding == null || binding.getUser() == null ? String.valueOf(account) : binding.getUser().getName();
    }

    public AlarmNotificationBinding getBinding() {
        return binding;
    }

    public AlarmSendLog setBinding(AlarmNotificationBinding binding) {
        this.binding = binding;
        if (binding != null && account != null) this.account = binding.getAccount();
        return this;
    }

    public String getAddress() {
        return address;
    }

    public AlarmSendLog setAddress(String address) {
        this.address = address;
        return this;
    }

    public InternetAddress createInternetAddress() {
        try {
            String name = Optional.ofNullable(binding).map(AlarmNotificationBinding::getUser).map(User::getName).orElse(null);
            return name == null ? new InternetAddress(address) : new InternetAddress(address, name, StandardCharsets.UTF_8.name());
        } catch (AddressException | UnsupportedEncodingException e) {
            return null;
        }
    }

    public String getTitle() {
        return title;
    }

    public AlarmSendLog setTitle(String title) {
        this.title = title;
        return this;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public AlarmSendLog setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public AlarmSendLog setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
        return this;
    }

    public AlarmSendStatus getStatus() {
        return status;
    }

    public AlarmSendLog setStatus(String status) {
        this.status = AlarmSendStatus.parse(status);
        return this;
    }

    public AlarmSendLog setStatus(AlarmSendStatus status) {
        this.status = status;
        return this;
    }

    public String getStatusName() {
        return status == null ? null : status.name();
    }

    public String getMessage() {
        return message;
    }

    public AlarmSendLog setMessage(String message) {
        this.message = message;
        return this;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public AlarmSendLog setAttachment(Blob blob) {
        try {
            this.attachment = blob.getBinaryStream().readAllBytes();
        } catch (Exception e) {
            this.attachment = null;
        }
        return this;
    }

    public AlarmSendLog setAttachment(byte[] attachment) {
        this.attachment = attachment;
        return this;
    }

    public Blob getAttachmentBlob(Connection connection) {
        try {
            Blob blob = connection.createBlob();
            blob.setBytes(0, attachment);
            return blob;
        } catch (Exception e) {
            return null;
        }
    }

    public JsonObject getData() {
        return data;
    }

    public AlarmSendLog setData(String data) {
        this.data = new GsonJsonObjectUtil(data).get();
        return this;
    }

    public AlarmSendLog setData(JsonObject data) {
        this.data = data;
        return this;
    }

    public String getErrorStack() {
        return errorStack;
    }

    public AlarmSendLog setErrorStack(String errorStack) {
        this.errorStack = errorStack;
        return this;
    }

    public AlarmSendLog setErrorStack(Throwable error) {
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        this.errorStack = writer.toString();
        return this;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public AlarmSendLog setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AlarmSendLog setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public boolean isResend() {
        return resend;
    }

    public AlarmSendLog setResend(boolean resend) {
        this.resend = resend;
        return this;
    }

    public AlarmSendLog setFailedSendStatus() {
        this.status = resend ? AlarmSendStatus.重新发送失败 : AlarmSendStatus.发送失败;
        return this;
    }

    public AlarmSendLog setSendingStatus() {
        this.status = resend ? AlarmSendStatus.重新发送中 : AlarmSendStatus.发送中;
        return this;
    }

    public AlarmSendLog setSuccessSendStatus() {
        this.status = resend ? AlarmSendStatus.重新发送成功 : AlarmSendStatus.发送成功;
        return this;
    }

    public AlarmSendLog clearError() {
        this.errorMessage = null;
        this.errorStack = null;
        return this;
    }

    @Override
    public String toString() {
        return GsonJsonObjectUtil.createGsonBuilder().create().toJson(this);
    }

    public AlarmSendLogPO createPO() {
        return new AlarmSendLogPO()
                .setId(this.getId())
                .setAid(this.getAid())
                .setAlarmId(this.getAlarmId())
                .setPolicyId(this.getPolicyId())
                .setChannel(this.getChannel())
                .setAccount(this.getAccount())
                .setAddress(this.getAddress())
                .setTitle(this.getTitle())
                .setCreateTime(LocalDateTime.now())
                .setSendTime(this.getSendTime())
                .setStatus(this.getStatus())
                .setMessage(this.getMessage())
                .setAttachmentName(this.getAttachmentName())
                .setAttachment(this.getAttachment())
                .setData(this.getData())
                .setErrorMessage(this.getErrorMessage())
                .setErrorStack(this.getErrorStack())
                ;
    }
}
