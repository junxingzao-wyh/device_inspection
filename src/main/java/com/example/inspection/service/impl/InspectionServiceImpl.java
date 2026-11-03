package com.example.inspection.service.impl;

import com.example.inspection.common.BusinessException;
import com.example.inspection.common.PageResult;
import com.example.inspection.entity.InspectionPlan;
import com.example.inspection.entity.InspectionRecord;
import com.example.inspection.entity.Notification;
import com.example.inspection.mapper.InspectionPlanMapper;
import com.example.inspection.mapper.InspectionRecordMapper;
import com.example.inspection.mapper.NotificationMapper;
import com.example.inspection.mapper.UserMapper;
import com.example.inspection.service.InspectionService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InspectionServiceImpl implements InspectionService {

    private final InspectionPlanMapper planMapper;
    private final InspectionRecordMapper recordMapper;
    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    public InspectionServiceImpl(InspectionPlanMapper planMapper,
                                 InspectionRecordMapper recordMapper,
                                 NotificationMapper notificationMapper,
                                 UserMapper userMapper) {
        this.planMapper = planMapper;
        this.recordMapper = recordMapper;
        this.notificationMapper = notificationMapper;
        this.userMapper = userMapper;
    }

    @Override
    public PageResult<InspectionPlan> planPage(Long deviceId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<InspectionPlan> list = planMapper.selectList(deviceId, offset, size);
        long total = planMapper.selectCount(deviceId);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public InspectionPlan getPlan(Long id) {
        return planMapper.selectById(id);
    }

    @Override
    public void savePlan(InspectionPlan plan) {
        if (plan.getDeviceId() == null) throw new BusinessException("请选择设备");
        if (plan.getInspectorId() == null) throw new BusinessException("请选择巡检员");
        if (plan.getCycleDays() == null || plan.getCycleDays() <= 0) {
            throw new BusinessException("巡检周期必须大于 0");
        }
        plan.setId(null);
        // status 列非空且默认 1；表单未提交时必须显式补默认值，否则插入 NULL 会违反约束
        if (plan.getStatus() == null) plan.setStatus(1);
        planMapper.insert(plan);
    }

    @Override
    public void updatePlan(InspectionPlan plan) {
        planMapper.updateById(plan);
    }

    @Override
    public PageResult<InspectionRecord> recordPage(Long deviceId, String result, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<InspectionRecord> list = recordMapper.selectList(deviceId, result, offset, size);
        long total = recordMapper.selectCount(deviceId, result);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public InspectionRecord getRecord(Long id) {
        return recordMapper.selectById(id);
    }

    @Override
    public void execute(InspectionRecord record) {
        if (record.getInspectTime() == null) {
            record.setInspectTime(LocalDateTime.now());
        }
        recordMapper.insert(record);
        // 巡检异常 → 给所有管理员写通知
        if ("ABNORMAL".equals(record.getResult())) {
            List<Long> adminIds = userMapper.selectByRole("ADMIN")
                    .stream().map(u -> u.getId()).toList();
            for (Long adminId : adminIds) {
                Notification n = new Notification();
                n.setReceiverId(adminId);
                n.setTitle("巡检异常提醒");
                n.setContent("设备 id=" + record.getDeviceId() + " 巡检结果异常，请及时处理");
                n.setType("INSPECTION_ABNORMAL");
                n.setBizId(record.getId());
                notificationMapper.insert(n);
            }
        }
    }
}
