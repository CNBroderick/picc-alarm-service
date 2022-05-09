package ape.alarm.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 告警工单表
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AlarmWos implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 告警工单内部唯一ID
     */
    private Integer id;
    /**
     * 监控告警平台唯一标识
     */
    private String uuid;
    /**
     * 监控系统
     */
    private String imSource;
    /**
     * 事件工单的工单号
     */
    private String wfNum;
    /**
     * 事件工单的工单ID
     */
    private String wfId;
    /**
     * 当前同质连续监控告警次序
     */
    private Integer mntOrder;
    /**
     * 返回编码。0-表示成功，1…n-表示该批次传入失败（出现异常的话会根据传递过来的mntorder返回）
     */
    private Integer resCode;
    /**
     * 工单返回消息
     */
    private String resMessage;
    /**
     * 事件工单标题
     */
    private String wfTitle;
    /**
     * 事件工单描述
     */
    private String wfDesc;
    /**
     * 保存同质连续告警
     */
    private String isSave;
    /**
     * 同质告警结束
     */
    private String isEnd;
    /**
     * 工单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 工单关闭时间
     */
    private LocalDateTime closeTime;
    /**
     * 工单创建状态：待创建、创建失败、已创建、已关闭
     */
    private String status;
}
