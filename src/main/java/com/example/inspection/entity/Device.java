package com.example.inspection.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 设备实体
 * 显式 getter/setter，避免依赖 Lombok 注解处理器
 */
public class Device {
    private Long id;
    private String deviceNo;
    private String name;
    private String category;
    private String location;
    /** 责任人 */
    private Long responsibleUserId;
    /** NORMAL / REPAIRING / DISABLED */
    private String status;
    private LocalDate purchaseDate;
    /** 维保周期（天） */
    private Integer maintenanceCycleDays;
    private LocalDate lastMaintenanceDate;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    /** 关联：责任人姓名（查询时联表填充） */
    private String responsibleUserName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceNo() { return deviceNo; }
    public void setDeviceNo(String deviceNo) { this.deviceNo = deviceNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Long getResponsibleUserId() { return responsibleUserId; }
    public void setResponsibleUserId(Long responsibleUserId) { this.responsibleUserId = responsibleUserId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public Integer getMaintenanceCycleDays() { return maintenanceCycleDays; }
    public void setMaintenanceCycleDays(Integer maintenanceCycleDays) { this.maintenanceCycleDays = maintenanceCycleDays; }
    public LocalDate getLastMaintenanceDate() { return lastMaintenanceDate; }
    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) { this.lastMaintenanceDate = lastMaintenanceDate; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
    public String getResponsibleUserName() { return responsibleUserName; }
    public void setResponsibleUserName(String responsibleUserName) { this.responsibleUserName = responsibleUserName; }
}
