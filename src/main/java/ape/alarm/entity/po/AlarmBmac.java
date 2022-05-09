package ape.alarm.entity.po;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 告警蓝鲸告警中心发送信息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class AlarmBmac implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 蓝鲸告警唯一ID
     */
    private Integer id;
    /**
     * 告警聚合唯一ID
     */
    private Integer aid;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 关闭时间
     */
    private LocalDateTime closeTime;
    /**
     * 告警发送状态：待创建、创建失败、已创建、已关闭
     */
    private String status;
    /**
     * 返回状态码：1200为成功，其余为失败
     */
    private Integer code;
    /**
     * 告警对象
     */
    private String ip;
    /**
     * 告警指标
     */
    private String alarmType;
    /**
     * 告警级别，枚举：remain/warning/fatal(提醒/警告/致命)
     */
    private String level;
    /**
     * 告警事件动作，枚举：firing/resolved(产生告警/消除告警)
     */
    private String action;
    /**
     * 请求失败返回的错误消息
     */
    private String message;

    public AlarmBmacData createData(JsonObject jsonObject) {
        return new AlarmBmacData(id, aid, jsonObject);
    }

}
