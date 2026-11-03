package com.example.inspection.service;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.MaintenanceRecord;

public interface MaintenanceService {
    PageResult<MaintenanceRecord> pageList(Long deviceId, int page, int size);

    MaintenanceRecord getById(Long id);

    void save(MaintenanceRecord record);
}
