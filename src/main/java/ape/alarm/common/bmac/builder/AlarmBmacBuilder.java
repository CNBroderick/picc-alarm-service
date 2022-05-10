package ape.alarm.common.bmac.builder;

import ape.alarm.common.bmac.core.AlarmBmacAction;
import ape.alarm.common.bmac.core.AlarmBmacLevel;
import ape.alarm.common.bmac.core.AlarmBmacStatus;
import ape.alarm.common.core.AlarmContentRenderManger;
import ape.alarm.common.email.AlarmEmailRenderRecordset;
import ape.alarm.common.email.AlarmTitleFactory;
import ape.alarm.common.email.builder.AbstractAlarmEmailBuilder;
import ape.alarm.entity.alarm.ApeAlarm;
import ape.alarm.entity.po.AlarmBmac;
import ape.alarm.entity.po.AlarmBmacData;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class AlarmBmacBuilder {

    private final ApeAlarm alarm;
    private AlarmBmac alarmBmac;
    private GsonJsonObjectUtil postData;

    public AlarmBmacBuilder(ApeAlarm alarm) {
        this.alarm = alarm;
    }


    public AlarmBmacBuilder build() {
        this.alarmBmac = createEntity();
        this.postData = createPostData();
        return this;
    }

    private AlarmBmac createEntity() {
        return new AlarmBmac()
                .aid(alarm.getId())
                .alarmType(AlarmTitleFactory.getInstance().translateAlarmType(alarm))
                .createTime(LocalDateTime.now())
                .action(AlarmBmacAction.firing.name())
                .status(AlarmBmacStatus.待创建.name())
                .ip(alarm.getAlarmId())
                .level(AlarmBmacLevel.warning.name())
                ;
    }

    private AlarmBmacData createAlarmBmacData(int bmacId) {
        return new AlarmBmacData().id(bmacId).aid(alarm.getId()).request(createPostData().get());
    }

    private String createTitle() {
        return AlarmTitleFactory.getInstance().createAlarmEmailTitle(alarm);
    }

    private String createDescription() {
        AlarmEmailRenderRecordset renderRecordset = new AlarmEmailRenderRecordset();
        AbstractAlarmEmailBuilder builder = AlarmContentRenderManger.getInstance().get(alarm.getAlarmType());
        builder.createTable(alarm, renderRecordset);
        return builder.createSummary(alarm).replaceFirst("，告警详情如下：", "。") + "\n\n" + Arrays.stream(renderRecordset.getRecords())
                .map(record -> record.getString(AlarmEmailRenderRecordset.FIELD_NAME) + "：" + record.getString(AlarmEmailRenderRecordset.FIELD_VALUE))
                .collect(Collectors.joining("\n"));
    }


    private GsonJsonObjectUtil createPostData() {
        JsonObject jsonObject = new JsonObject();

        AlarmEmailRenderRecordset renderRecordset = new AlarmEmailRenderRecordset();
        AbstractAlarmEmailBuilder builder = AlarmContentRenderManger.getInstance().get(alarm.getAlarmType());
        builder.createTable(alarm, renderRecordset);
        String meta_info = Arrays.stream(renderRecordset.getRecords()).map(record ->
                record.getString(AlarmEmailRenderRecordset.FIELD_NAME) + "：" + record.getString(AlarmEmailRenderRecordset.FIELD_VALUE)
        ).collect(Collectors.joining("\n"));
        jsonObject.addProperty("ip", alarmBmac.ip());
        jsonObject.addProperty("source_time", LocalDateTimeFormatter.Short(alarm.getAlarmTime()));
        jsonObject.addProperty("alarm_type", alarmBmac.alarmType());
        jsonObject.addProperty("level", alarmBmac.level());
        jsonObject.addProperty("alarm_name", createTitle());
        jsonObject.addProperty("alarm_content", builder.createSummary(alarm));
        jsonObject.addProperty("meta_info", meta_info);
        jsonObject.addProperty("action", alarmBmac.action());
        jsonObject.addProperty("bk_obj_id", "");
        jsonObject.addProperty("bk_inst_id", 0);

        return new GsonJsonObjectUtil(jsonObject);
    }

}
