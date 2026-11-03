package com.example.inspection.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 巡检计划实体
 * 显式 getter/setter，避免依赖 Lombok 注解处理器
 */
public class InspectionPlan {
    private Long id;
    private Long deviceId;
    private Long inspectorId;
    /** 巡检周期（天） */
    private Integer cycleDays;
    private LocalDate nextInspectionDate;
    /** 1 启用 0 停用 */
    private Integer status;
    private LocalDateTime createTime;
    /** 关联字段 */
    private String deviceName;
    private String deviceNo;
    private String inspectorName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getInspectorId() { return inspectorId; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }
    public Integer getCycleDays() { return cycleDays; }
    public void setCycleDays(Integer cycleDays) { this.cycleDays = cycleDays; }
    public LocalDate getNextInspectionDate() { return nextInspectionDate; }
    public void setNextInspectionDate(LocalDate nextInspectionDate) { this.nextInspectionDate = nextInspectionDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getDeviceNo() { return deviceNo; }
    public void setDeviceNo(String deviceNo) { this.deviceNo = deviceNo; }
    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
}
