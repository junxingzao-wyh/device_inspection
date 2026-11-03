package com.example.inspection.service.impl;

import com.example.inspection.common.BusinessException;
import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Device;
import com.example.inspection.entity.Notification;
import com.example.inspection.entity.RepairOrder;
import com.example.inspection.mapper.DeviceMapper;
import com.example.inspection.mapper.NotificationMapper;
import com.example.inspection.mapper.RepairOrderMapper;
import com.example.inspection.service.RepairOrderService;
import com.example.inspection.util.OrderNoGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderMapper repairOrderMapper;
    private final DeviceMapper deviceMapper;
    private final NotificationMapper notificationMapper;
    private final OrderNoGenerator orderNoGenerator;

    public RepairOrderServiceImpl(RepairOrderMapper repairOrderMapper,
                                 DeviceMapper deviceMapper,
                                 NotificationMapper notificationMapper,
                                 OrderNoGenerator orderNoGenerator) {
        this.repairOrderMapper = repairOrderMapper;
        this.deviceMapper = deviceMapper;
        this.notificationMapper = notificationMapper;
        this.orderNoGenerator = orderNoGenerator;
    }

    @Override
    public PageResult<RepairOrder> pageList(String status, String keyword, Long deviceId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<RepairOrder> list = repairOrderMapper.selectList(status, keyword, deviceId, offset, size);
        long total = repairOrderMapper.selectCount(status, keyword, deviceId);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public RepairOrder getById(Long id) {
        RepairOrder o = repairOrderMapper.selectById(id);
        if (o == null) throw new BusinessException("工单不存在");
        return o;
    }

    @Override
    @Transactional
    public void create(RepairOrder order) {
        order.setId(null);
        order.setOrderNo(orderNoGenerator.next());
        order.setStatus("PENDING_ASSIGN");
        repairOrderMapper.insert(order);

        // 设备状态改为 REPAIRING
        Device dev = deviceMapper.selectById(order.getDeviceId());
        if (dev != null) {
            Device upd = new Device();
            upd.setId(dev.getId());
            upd.setStatus("REPAIRING");
            deviceMapper.updateById(upd);
        }
    }

    @Override
    @Transactional
    public void assign(Long id, Long handlerId) {
        RepairOrder o = getById(id);
        if (!"PENDING_ASSIGN".equals(o.getStatus())) {
            throw new BusinessException("当前状态 " + o.getStatus() + " 不允许派单");
        }
        RepairOrder upd = new RepairOrder();
        upd.setId(id);
        upd.setHandlerId(handlerId);
        upd.setStatus("PROCESSING");
        upd.setAssignTime(LocalDateTime.now());
        repairOrderMapper.updateById(upd);

        // 通知维修员
        Notification n = new Notification();
        n.setReceiverId(handlerId);
        n.setTitle("有新工单待处理");
        n.setContent("工单 " + o.getOrderNo() + " 已派发给你");
        n.setType("ORDER_ASSIGN");
        n.setBizId(id);
        notificationMapper.insert(n);
    }

    @Override
    public void process(Long id) {
        RepairOrder o = getById(id);
        if ("CLOSED".equals(o.getStatus()) || "COMPLETED".equals(o.getStatus())
                || "PENDING_ACCEPT".equals(o.getStatus())) {
            throw new BusinessException("当前状态不允许开始处理");
        }
        if ("PROCESSING".equals(o.getStatus())) return;
        RepairOrder upd = new RepairOrder();
        upd.setId(id);
        upd.setStatus("PROCESSING");
        repairOrderMapper.updateById(upd);
    }

    @Override
    @Transactional
    public void finish(Long id, String handleRemark) {
        RepairOrder o = getById(id);
        if (!"PROCESSING".equals(o.getStatus())) {
            throw new BusinessException("当前状态 " + o.getStatus() + " 不允许完成处理");
        }
        RepairOrder upd = new RepairOrder();
        upd.setId(id);
        upd.setStatus("PENDING_ACCEPT");
        upd.setHandleRemark(handleRemark);
        upd.setFinishTime(LocalDateTime.now());
        repairOrderMapper.updateById(upd);
    }

    @Override
    @Transactional
    public void accept(Long id) {
        RepairOrder o = getById(id);
        if (!"PENDING_ACCEPT".equals(o.getStatus())) {
            throw new BusinessException("当前状态 " + o.getStatus() + " 不允许验收");
        }
        RepairOrder upd = new RepairOrder();
        upd.setId(id);
        upd.setStatus("COMPLETED");
        upd.setAcceptTime(LocalDateTime.now());
        repairOrderMapper.updateById(upd);

        // 设备状态改回 NORMAL
        Device dev = deviceMapper.selectById(o.getDeviceId());
        if (dev != null) {
            Device du = new Device();
            du.setId(dev.getId());
            du.setStatus("NORMAL");
            deviceMapper.updateById(du);
        }

        // 通知维修员和报修人
        for (Long uid : List.of(o.getHandlerId(), o.getReporterId())) {
            if (uid != null) {
                Notification n = new Notification();
                n.setReceiverId(uid);
                n.setTitle("工单已验收通过");
                n.setContent("工单 " + o.getOrderNo() + " 已验收完成");
                n.setType("ORDER_ACCEPT");
                n.setBizId(id);
                notificationMapper.insert(n);
            }
        }
    }

    @Override
    public void close(Long id) {
        RepairOrder o = getById(id);
        if ("CLOSED".equals(o.getStatus())) return;
        RepairOrder upd = new RepairOrder();
        upd.setId(id);
        upd.setStatus("CLOSED");
        repairOrderMapper.updateById(upd);
    }
}
