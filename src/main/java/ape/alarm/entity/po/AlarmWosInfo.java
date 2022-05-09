package ape.alarm.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 告警工单详情
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlarmWosInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 告警工单详情唯一ID
     */
    private Integer id;
    /**
     * 告警工单内部唯一ID
     */
    private Integer wosId;
    /**
     * 告警唯一ID
     */
    private Integer alarmId;
    /**
     * 受影响系统
     */
    private String affectSystem;
    /**
     * 错误系统
     */
    private String errorSystem;
    /**
     * 事件发生时间
     */
    private LocalDateTime placeTime;
    /**
     * 影响比例
     */
    private BigDecimal affectRate;
    /**
     * 影响振幅
     */
    private BigDecimal affectAmplitude;
    /**
     * 工单标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 业务受影响类型
     */
    private String affectType;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 影响范围
     */
    private String affectArea;
    /**
     * 故障恢复时间
     */
    private LocalDateTime recoveryTime;
    /**
     * 故障时长
     */
    private String faultDuration;
    /**
     * 影响功能
     */
    private String impactFunction;
    /**
     * 发送工单时间
     */
    private LocalDateTime sendTime;
}
