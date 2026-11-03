package com.example.inspection.service;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.RepairOrder;

public interface RepairOrderService {
    /**
     * 分页查询工单
     * @param status 状态过滤，可空
     * @param keyword 关键字（匹配工单号/设备名），可空
     * @param deviceId 设备过滤，可空
     */
    PageResult<RepairOrder> pageList(String status, String keyword, Long deviceId, int page, int size);

    RepairOrder getById(Long id);

    /** 发起报修，自动生成工单号 */
    void create(RepairOrder order);

    /** 派单：PENDING_ASSIGN → PROCESSING */
    void assign(Long id, Long handlerId);

    /** 开始处理：PROCESSING → PROCESSING（校验当前为 PROCESSING 或 PENDING_ASSIGN） */
    void process(Long id);

    /** 处理完成：PROCESSING → PENDING_ACCEPT */
    void finish(Long id, String handleRemark);

    /** 验收：PENDING_ACCEPT → COMPLETED，同时设备状态改回 NORMAL */
    void accept(Long id);

    /** 关闭：任意状态 → CLOSED */
    void close(Long id);
}
