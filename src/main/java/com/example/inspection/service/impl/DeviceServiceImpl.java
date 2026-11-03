package com.example.inspection.service.impl;

import com.example.inspection.common.BusinessException;
import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Device;
import com.example.inspection.mapper.DeviceMapper;
import com.example.inspection.service.DeviceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceMapper deviceMapper) {
        this.deviceMapper = deviceMapper;
    }

    @Override
    public PageResult<Device> pageList(String keyword, String status, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<Device> list = deviceMapper.selectList(keyword, status, offset, size);
        long total = deviceMapper.selectCount(keyword, status);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public Device getById(Long id) {
        Device d = deviceMapper.selectById(id);
        if (d == null) throw new BusinessException("设备不存在");
        return d;
    }

    @Override
    public void save(Device device) {
        device.setId(null);
        deviceMapper.insert(device);
    }

    @Override
    public void update(Device device) {
        if (device.getId() == null) throw new BusinessException("id 不能为空");
        deviceMapper.updateById(device);
    }

    @Override
    public void disable(Long id) {
        Device device = new Device();
        device.setId(id);
        device.setStatus("DISABLED");
        deviceMapper.updateById(device);
    }
}
