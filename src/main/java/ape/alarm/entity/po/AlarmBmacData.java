package ape.alarm.entity.po;

import ape.alarm.common.bmac.core.AlarmBmacAction;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bklab.quark.util.time.LocalDateTimeFormatter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 告警蓝鲸告警中心发送信息日志表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public class AlarmBmacData implements Serializable {
    /**
     * 蓝鲸告警唯一ID
     */
    private Integer id;

    /**
     * 告警聚合唯一ID
     */
    private Integer aid;

    /**
     * 返回状态码：1200为成功，其余为失败
     */
    private Integer code;

    /**
     * 告警恢复返回状态码：1200为成功，其余为失败
     */
    private Integer restoreCode;

    /**
     * 发送数据
     */
    private JsonObject request;

    /**
     * 返回数据
     */
    private JsonObject response;

    /**
     * 告警恢复发送数据
     */
    private JsonObject restoreRequest;

    /**
     * 告警恢复返回数据
     */
    private JsonObject restoreResponse;

    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 推送告警错误信息
     */
    private String requestException;
    /**
     * 恢复告警错误信息
     */
    private String restoreException;

    public AlarmBmacData(Integer id, Integer aid, JsonObject request) {
        this.id = id;
        this.aid = aid;
        this.request = request;
    }

    public AlarmBmacData createRestoreRequest(LocalDateTime restoreTime) {
        this.restoreRequest = request.deepCopy();
        restoreRequest.addProperty("action", AlarmBmacAction.resolved.name());
        restoreRequest.addProperty("close_time", LocalDateTimeFormatter.Short(restoreTime));
        return this;
    }

}
