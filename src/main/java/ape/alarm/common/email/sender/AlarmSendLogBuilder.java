package ape.alarm.common.email.sender;

import ape.alarm.common.core.AlarmContentRenderManger;
import ape.alarm.common.email.AlarmTitleFactory;
import ape.alarm.common.email.builder.AbstractAlarmEmailBuilder;
import ape.alarm.common.email.builder.AlarmEmailBuildResult;
import ape.alarm.entity.alarm.AlarmSendLog;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.service.common.AlarmSendLogService;
import ape.master.common.email.EmailMimeMessageBuilder;
import ape.master.common.parameter.ApeParameter;
import ape.master.common.util.ApplicationContextUtils;
import ape.master.entity.alarm.po.AlarmSendLogPO;
import ape.master.entity.alarm.po.AlarmSendStatus;
import ape.master.entity.alarm.transmission.AlarmContactChannelTypeEnum;
import ape.master.entity.alarm.transmission.AlarmNotificationBinding;
import ape.master.entity.alarm.transmission.AlarmNotificationPolicy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AlarmSendLogBuilder implements IAlarmEmailSender {

    private static final AlarmContactChannelTypeEnum channel = AlarmContactChannelTypeEnum.邮件;

    @Override
    public boolean send(List<AlarmNotificationPolicy> notificationPolicies, ApeAlarm alarm) {
        if (!AlarmSendEmailControlManager.getInstance().testHasNearlySendAlarm(alarm)) {
            log.debug("alarm " + alarm.getAlarmId() + " has nearly send alarm.");
            return true;
        }

        Set<AlarmNotificationBinding> bindings = getAlarmContacts(notificationPolicies, channel);
        AbstractAlarmEmailBuilder builder = AlarmContentRenderManger.getInstance().get(alarm.getAlarmType());
        if (builder == null) return true;
        boolean success = true;

        for (AlarmNotificationBinding binding : bindings) {
            log.debug("alarm %s@%s#%s beyond similar alarms max daily sent times.".formatted(
                    alarm.getAlarmId(), binding.getAccount(), binding.getPolicy().getName())
            );

            if (!AlarmSendEmailControlManager.getInstance().testSimilarAlarmsMaxDailySentTimes(binding, alarm)) {
                continue;
            }

            String address = binding.getChannel(AlarmSendLogBuilder.channel);

            AlarmSendLog alarmSendLog = new AlarmSendLog().setResend(false).setAccount(binding.getAccount())
                    .setAlarm(alarm).setPolicy(binding.getPolicy()).setBinding(binding).setChannel(channel)
                    .setAddress(address).setTitle(AlarmTitleFactory.getInstance().createAlarmEmailTitle(alarm));
            save(alarmSendLog);
            if (alarmSendLog.getId() < 0) continue;

            AlarmSendEmailControlManager.getInstance().addSendCount(alarm.getAlarmId(), binding.getAccount());

            success &= prepareSend(builder, alarmSendLog, address);
        }

        return success;
    }

    @Override
    public boolean sendRestored(List<AlarmSendLog> sources) {
        AlarmSendLogService alarmSendLogPOService = ApplicationContextUtils.getBean(AlarmSendLogService.class);
        List<AlarmSendLog> alarmSendLogs = sources.parallelStream().map(source -> new AlarmSendLog()
                .setResend(false).setAccount(source.getAccount()).setAlarm(source.getAlarm())
                .setPolicy(source.getPolicy()).setBinding(source.getBinding()).setChannel(channel)
                .setAddress(source.getAddress())
                .setTitle(AlarmTitleFactory.getInstance().createAlarmEmailTitle(source.getAlarm(), true))
        ).filter(alarmSendLog -> {
            AlarmEmailBuildResult buildResult;
            try {
                buildResult = AlarmContentRenderManger.getInstance().get(alarmSendLog.getAlarm().getAlarmType()).build(alarmSendLog.getAlarm(), alarmSendLog.getContactName(), true);
                alarmSendLog.setAttachmentName(buildResult.getAttachmentName()).setAttachment(buildResult.getAttachment()).setMessage(buildResult.getMessage());
                alarmSendLogPOService.save(alarmSendLog);
                return true;
            } catch (Exception exception) {
                LoggerFactory.getLogger(getClass()).error("初始化发送内容失败。\n" + alarmSendLog, exception);
                alarmSendLog.setFailedSendStatus().setErrorStack(exception).setErrorMessage("初始化发送内容失败。");
                return false;
            }
        }).collect(Collectors.toList());
        LoggerFactory.getLogger(getClass()).info("创建{}封待发送告警恢复邮件。", alarmSendLogs.size());

        Set<Integer> ids = alarmSendLogPOService.findAllByStatusIn(AlarmSendStatus.待发送)
                .stream().map(AlarmSendLogPO::getId).collect(Collectors.toSet());
        addSendingQueue(ids);
        LoggerFactory.getLogger(getClass()).info("添加{}封告警恢复邮件到发送队列。", ids.size());

        return false;
    }

    @Override
    public boolean resend(AlarmSendLog alarmSendLog) {
        AbstractAlarmEmailBuilder builder = AlarmContentRenderManger.getInstance().get(alarmSendLog.getAlarm().getAlarmType());
        alarmSendLog.setResend(true);
        return prepareSend(builder, alarmSendLog, alarmSendLog.getAddress());
    }

    private boolean prepareSend(AbstractAlarmEmailBuilder builder, AlarmSendLog alarmSendLog, String address) {
        InternetAddress internetAddress;
        try {
            internetAddress = new InternetAddress(address, alarmSendLog.getContactName());
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("邮件地址[" + address + "]错误。\n" + alarmSendLog, e);
            alarmSendLog.setFailedSendStatus().setErrorStack(e).setErrorMessage("邮件地址错误。");
            update(alarmSendLog);
            return false;
        }
        return processSend(builder, alarmSendLog, internetAddress);
    }

    private boolean processSend(AbstractAlarmEmailBuilder builder, AlarmSendLog alarmSendLog, InternetAddress address) {
        AlarmEmailBuildResult buildResult;
        try {
            buildResult = builder.build(alarmSendLog.getAlarm(), alarmSendLog.getContactName());
            alarmSendLog.setAttachmentName(buildResult.getAttachmentName())
                    .setAttachment(buildResult.getAttachment())
                    .setMessage(buildResult.getMessage())
                    .setAddress(address.getAddress());
        } catch (Exception exception) {
            LoggerFactory.getLogger(getClass()).error("初始化发送内容失败。\n" + alarmSendLog, exception);
            alarmSendLog.setFailedSendStatus().setErrorStack(exception).setErrorMessage("初始化发送内容失败。");
            return false;
        } finally {
            update(alarmSendLog);
        }
        addSendingQueue(alarmSendLog);

        return false;
    }

    @Deprecated
    private boolean send(AlarmSendLog alarmSendLog, AlarmEmailBuildResult buildResult, InternetAddress address) {
        MimeMessage mimeMessage;
        try {
            mimeMessage = new EmailMimeMessageBuilder(ApeParameter.getInstance()).createMimeMessage();
        } catch (Exception exception) {
            LoggerFactory.getLogger(getClass()).error("初始化MimeMessage失败。\n" + alarmSendLog, exception);
            alarmSendLog.setFailedSendStatus().setErrorStack(exception).setErrorMessage("初始化MimeMessage失败。");
            update(alarmSendLog);
            return false;
        } finally {
            update(alarmSendLog);
        }
        return send(mimeMessage, buildResult, alarmSendLog, address);
    }

    @Deprecated
    private boolean send(MimeMessage mimeMessage, AlarmEmailBuildResult buildResult, AlarmSendLog alarmSendLog, InternetAddress address) {
        try {

            MimeMultipart multipart = buildResult.getMimeMultipart();
            mimeMessage.setSubject(alarmSendLog.getTitle());
            mimeMessage.setSentDate(new Date());
            mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{address});
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

}
