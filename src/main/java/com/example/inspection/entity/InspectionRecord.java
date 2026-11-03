package com.example.inspection.entity;

import java.time.LocalDateTime;

/**
 * 巡检记录实体
 * 显式 getter/setter，避免依赖 Lombok 注解处理器
 */
public class InspectionRecord {
    private Long id;
    /** 可空，手动巡检时为空 */
    private Long planId;
    private Long deviceId;
    private Long inspectorId;
    /** NORMAL / ABNORMAL */
    private String result;
    private String description;
    private String photoName;
    private LocalDateTime inspectTime;
    private LocalDateTime createTime;
    /** 关联字段 */
    private String deviceName;
    private String deviceNo;
    private String inspectorName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getInspectorId() { return inspectorId; }
    public void setInspectorId(Long inspectorId) { this.inspectorId = inspectorId; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPhotoName() { return photoName; }
    public void setPhotoName(String photoName) { this.photoName = photoName; }
    public LocalDateTime getInspectTime() { return inspectTime; }
    public void setInspectTime(LocalDateTime inspectTime) { this.inspectTime = inspectTime; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getDeviceNo() { return deviceNo; }
    public void setDeviceNo(String deviceNo) { this.deviceNo = deviceNo; }
    public String getInspectorName() { return inspectorName; }
    public void setInspectorName(String inspectorName) { this.inspectorName = inspectorName; }
}
