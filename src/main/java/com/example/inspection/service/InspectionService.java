package com.example.inspection.service;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.InspectionPlan;
import com.example.inspection.entity.InspectionRecord;

public interface InspectionService {
    // 计划
    PageResult<InspectionPlan> planPage(Long deviceId, int page, int size);
    InspectionPlan getPlan(Long id);
    void savePlan(InspectionPlan plan);
    void updatePlan(InspectionPlan plan);

    // 记录
    PageResult<InspectionRecord> recordPage(Long deviceId, String result, int page, int size);
    InspectionRecord getRecord(Long id);
    /** 执行巡检：若结果异常，给管理员写通知 */
    void execute(InspectionRecord record);
}
