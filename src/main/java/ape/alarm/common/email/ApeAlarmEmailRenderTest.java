package ape.alarm.common.email;

import ape.alarm.common.email.render.AlarmEmailRender;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.operation.jdbc.alarm.ApeAlarmOperationEnum;
import ape.master.common.email.EmailMimeMessageBuilder;
import ape.master.common.parameter.ApeParameter;
import dataq.core.data.schema.Record;
import dataq.core.data.schema.*;
import dataq.core.jdbc.JdbcConnectionManager;
import org.bklab.quark.util.number.DigitalFormatter;
import org.bklab.quark.util.schema.RecordFactory;
import org.bklab.quark.util.schema.SchemaFactory;
import org.bklab.quark.util.time.RunningTime;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

class ApeAlarmEmailRenderTest {

    public final Recordset summarySet;
    private final RunningTime runningTime = new RunningTime();
    private final Recordset detailSet;

    public ApeAlarmEmailRenderTest() {
        Schema summary = new Schema();
        summary.addField(new Field("名称", DataType.STRING));
        summary.addField(new Field("值", DataType.STRING));

        summarySet = new SchemaFactory().string("名称", "值").recordset();
        new RecordFactory(summarySet.createRecord()).set("名称", "告警ID").set("值", "20210116");
        new RecordFactory(summarySet.createRecord()).set("名称", "分公司名称").set("值", "浙江省分公司");
        new RecordFactory(summarySet.createRecord()).set("名称", "应用名称").set("值", "统一门户-理赔前台-理赔核心业务前台页面");
        new RecordFactory(summarySet.createRecord()).set("名称", "开始时间").set("值", "2020-12-22 09:20:00");
        new RecordFactory(summarySet.createRecord()).set("名称", "结束时间").set("值", "2020-12-22 09:25:00");
        new RecordFactory(summarySet.createRecord()).set("名称", "关键步骤页名称").set("值", "/claim/carUndwrt/index");
        new RecordFactory(summarySet.createRecord()).set("名称", "页面加载时间异常数量").set("值", "57");
        new RecordFactory(summarySet.createRecord()).set("名称", "性能阈值数量").set("值", "12");
        new RecordFactory(summarySet.createRecord()).set("名称", "页面调用次数").set("值", "102");
        new RecordFactory(summarySet.createRecord()).set("名称", "占比").set("值", "28.22%");
        new RecordFactory(summarySet.createRecord()).set("名称", "告警阈值").set("值", "20.00%");
        new RecordFactory(summarySet.createRecord()).set("名称", "异常幅度").set("值", "2.33");
        new RecordFactory(summarySet.createRecord()).set("名称", "终端数量").set("值", "27");
        new RecordFactory(summarySet.createRecord()).set("名称", "异常终端数量").set("值", "5");

        Schema detail = new SchemaFactory().string("IP", "操作员").objectField("时间").string("关键页面", "完全加载时间").get();
        detailSet = new Recordset(detail);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new RecordFactory(detailSet.createRecord())
                    .set("IP", random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(255))
                    .set("操作员", Math.abs((random.nextInt() + 100000000) % 100000000) + "")
                    .set("时间", LocalDateTime.now().minusSeconds((long) (Math.random() * 300)))
                    .set("关键页面", "/claim/carUndwrt" + random.nextInt(10))
                    .set("完全加载时间", new DigitalFormatter(random.nextInt(5000) + 10000).toFormatted() + " ms");
        }
    }

    private ApeParameter getApeParameter() {
        return ApeParameter.getInstance().reload();
    }

    @Test
    public void testConnectionClosed() throws Exception {
        for (int i = 0; i < 10; i++) {
            List<ApeAlarm> alarmList = ApeAlarmOperationEnum.ApeAlarmQuery.createAbstractOperation()
                    .setParam("waitingSendAlarms", Boolean.TRUE).execute().asList();
            printJdbcPoolMap();
        }
    }

    public void printJdbcPoolMap() throws Exception {
        StopWatch stopWatch = new StopWatch("打印DataQ JDBC 连接池");
        stopWatch.start("打印DataQ JDBC 连接池");
        JdbcConnectionManager connectionManager = JdbcConnectionManager.getInstance();
        java.lang.reflect.Field field = connectionManager.getClass().getDeclaredField("poolMap");
        field.setAccessible(true);
        Map<?, ?> poolMap = (Map<?, ?>) field.get(connectionManager);

        for (Map.Entry<?, ?> entry : poolMap.entrySet()) {
            java.lang.reflect.Field f = entry.getValue().getClass().getDeclaredField("pool");
            f.setAccessible(true);
            BlockingQueue<?> o = (BlockingQueue<?>) f.get(entry.getValue());
            if (o.size() > 0) {
                System.out.println(entry.getKey() + ": " + o.size());
                LoggerFactory.getLogger(getClass()).warn(entry.getKey() + ": " + o.size());
            }
        }

        stopWatch.prettyPrint();
        stopWatch.stop();
    }

    @Test
    public void testExcel() throws Exception {
        runningTime.print("初始化完成");
        String body = createBody();
        runningTime.print("创建正文完成");
        ByteArrayOutputStream stream = createAttachments();
        runningTime.print("创建附件完成");

        Path path = Files.write(Files.createTempFile("ape-alarm-", ".xlsx"), stream.toByteArray());
        Desktop.getDesktop().open(path.toFile());
        System.out.println(path);
    }

    @Test
    public void test() throws Exception {
        runningTime.print("初始化完成");
        String body = createBody();
        runningTime.print("创建正文完成");
        ByteArrayOutputStream stream = createAttachments();
        runningTime.print("创建附件完成");

        MimeMessage mimeMessage = new EmailMimeMessageBuilder(getApeParameter()).createMimeMessage();
        mimeMessage.setSubject("应用监控-页面加载性能告警");
        mimeMessage.setSentDate(new Date());
        mimeMessage.setRecipients(Message.RecipientType.TO, new InternetAddress[]{
                new InternetAddress("z@bkLab.org")
        });

        MimeMultipart multipart = new MimeMultipart("mixed");
        InternetHeaders internetHeaders = new InternetHeaders();
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContentLanguage(new String[]{"zh-cn"});
        textPart.setText(body, "UTF-8");
        textPart.setHeader("Content-Type", "text/html; charset=UTF-8");
        multipart.addBodyPart(textPart);

        MimeBodyPart filePart = new MimeBodyPart();
        DataSource source = new ByteArrayDataSource(new ByteArrayInputStream(stream.toByteArray()), "application/msexcel");
        filePart.setDataHandler(new DataHandler(source));
        filePart.setFileName(MimeUtility.encodeText("应用监控-页面加载性能告警-" + DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss").format(LocalDateTime.now()) + ".xlsx"));
        multipart.addBodyPart(filePart);

        mimeMessage.setContent(multipart);
        mimeMessage.saveChanges();

        runningTime.print("初始化邮件消息完成");

        Transport.send(mimeMessage);
        runningTime.print("邮件发送完毕");
    }

    public String createBody() throws Exception {
        AlarmEmailRender emailRender = new AlarmEmailRender()
                .setAlarmSummary("页面加载性能告警").setRecipientName("z@bkLab.org");

        for (Record record : summarySet.asList()) {
            emailRender.addData(record.getString("名称"), record.getString("值"));
        }

        return emailRender.body();
    }

    public ByteArrayOutputStream createAttachments() throws Exception {
        return new AlarmExcelAttachmentBuilder("页面加载性能告警")
                .createSheet("汇总", summarySet.getSchema(), summarySet.asList())
                .createSheet("明细", detailSet.getSchema(), detailSet.asList())
                .createDownloadStream();
    }
}
