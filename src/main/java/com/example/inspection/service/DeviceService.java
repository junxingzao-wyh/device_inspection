package com.example.inspection.service;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Device;

public interface DeviceService {
    PageResult<Device> pageList(String keyword, String status, int page, int size);

    Device getById(Long id);

    void save(Device device);

    void update(Device device);

    /** 停用：软删除，status 置为 DISABLED */
    void disable(Long id);
}
