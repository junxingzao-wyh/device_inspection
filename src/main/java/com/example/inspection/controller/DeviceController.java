package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.Device;
import com.example.inspection.service.DeviceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public Result<PageResult<Device>> list(@RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) String status) {
        return Result.ok(deviceService.pageList(keyword, status, page, size));
    }

    @GetMapping("/{id}")
    public Result<Device> detail(@PathVariable Long id) {
        return Result.ok(deviceService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody Device device) {
        deviceService.save(device);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Device device) {
        device.setId(id);
        deviceService.update(device);
        return Result.ok();
    }

    @PutMapping("/{id}/disable")
    public Result<Void> disable(@PathVariable Long id) {
        deviceService.disable(id);
        return Result.ok();
    }
}
