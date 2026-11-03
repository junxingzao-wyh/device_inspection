package com.example.inspection.entity;

import java.time.LocalDateTime;

/**
 * 通知实体
 * 显式 getter/setter，避免依赖 Lombok 注解处理器
 */
public class Notification {
    private Long id;
    private Long receiverId;
    private String title;
    private String content;
    /** ORDER_ASSIGN / ORDER_ACCEPT / INSPECTION_ABNORMAL / MAINTENANCE_DUE */
    private String type;
    /** 关联业务 id */
    private Long bizId;
    /** 0 未读 1 已读 */
    private Integer isRead;
    private LocalDateTime createTime;
    /** 关联字段 */
    private String receiverName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Long getBizId() { return bizId; }
    public void setBizId(Long bizId) { this.bizId = bizId; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
}
