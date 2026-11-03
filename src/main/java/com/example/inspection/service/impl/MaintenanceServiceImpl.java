package com.example.inspection.service.impl;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Device;
import com.example.inspection.entity.MaintenanceRecord;
import com.example.inspection.mapper.DeviceMapper;
import com.example.inspection.mapper.MaintenanceRecordMapper;
import com.example.inspection.service.MaintenanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRecordMapper mapper;
    private final DeviceMapper deviceMapper;

    public MaintenanceServiceImpl(MaintenanceRecordMapper mapper, DeviceMapper deviceMapper) {
        this.mapper = mapper;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public PageResult<MaintenanceRecord> pageList(Long deviceId, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<MaintenanceRecord> list = mapper.selectList(deviceId, offset, size);
        long total = mapper.selectCount(deviceId);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public MaintenanceRecord getById(Long id) {
        return mapper.selectById(id);
    }

    @Override
    public void save(MaintenanceRecord record) {
        record.setId(null);
        mapper.insert(record);

        // 同步更新设备 last_maintenance_date
        if (record.getDeviceId() != null && record.getMaintenanceDate() != null) {
            Device upd = new Device();
            upd.setId(record.getDeviceId());
            upd.setLastMaintenanceDate(record.getMaintenanceDate());
            deviceMapper.updateById(upd);
        }
    }
}
