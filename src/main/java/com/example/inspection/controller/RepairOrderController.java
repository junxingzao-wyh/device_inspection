package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.RepairOrder;
import com.example.inspection.service.RepairOrderService;
import com.example.inspection.util.FileStorageService;
import com.example.inspection.util.SessionUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/repair-orders")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;
    private final FileStorageService fileStorageService;

    public RepairOrderController(RepairOrderService repairOrderService, FileStorageService fileStorageService) {
        this.repairOrderService = repairOrderService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public Result<PageResult<RepairOrder>> list(@RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(required = false) String status,
                                                 @RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) Long deviceId) {
        return Result.ok(repairOrderService.pageList(status, keyword, deviceId, page, size));
    }

    @GetMapping("/{id}")
    public Result<RepairOrder> detail(@PathVariable Long id) {
        return Result.ok(repairOrderService.getById(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    public Result<Void> create(@RequestParam Long deviceId,
                               @RequestParam String title,
                               @RequestParam(required = false) String description,
                               @RequestParam(required = false) MultipartFile photo) {
        RepairOrder order = new RepairOrder();
        order.setDeviceId(deviceId);
        order.setReporterId(SessionUtil.getLoginUserId());
        order.setTitle(title);
        order.setDescription(description);
        if (photo != null && !photo.isEmpty()) {
            order.setPhotoName(fileStorageService.store(photo));
        }
        repairOrderService.create(order);
        return Result.ok();
    }

    @PutMapping("/{id}/assign")
    public Result<Void> assign(@PathVariable Long id, @RequestBody java.util.Map<String, Long> body) {
        repairOrderService.assign(id, body.get("handlerId"));
        return Result.ok();
    }

    @PutMapping("/{id}/process")
    public Result<Void> process(@PathVariable Long id) {
        repairOrderService.process(id);
        return Result.ok();
    }

    @PutMapping("/{id}/finish")
    public Result<Void> finish(@PathVariable Long id, @RequestBody java.util.Map<String, String> body) {
        repairOrderService.finish(id, body.get("handleRemark"));
        return Result.ok();
    }

    @PutMapping("/{id}/accept")
    public Result<Void> accept(@PathVariable Long id) {
        repairOrderService.accept(id);
        return Result.ok();
    }

    @PutMapping("/{id}/close")
    public Result<Void> close(@PathVariable Long id) {
        repairOrderService.close(id);
        return Result.ok();
    }
}
