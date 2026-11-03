package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.MaintenanceRecord;
import com.example.inspection.service.MaintenanceService;
import com.example.inspection.util.FileStorageService;
import com.example.inspection.util.SessionUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    private final MaintenanceService maintenanceService;
    private final FileStorageService fileStorageService;

    public MaintenanceRecordController(MaintenanceService maintenanceService, FileStorageService fileStorageService) {
        this.maintenanceService = maintenanceService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public Result<PageResult<MaintenanceRecord>> list(@RequestParam(defaultValue = "1") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(required = false) Long deviceId) {
        return Result.ok(maintenanceService.pageList(deviceId, page, size));
    }

    @GetMapping("/{id}")
    public Result<MaintenanceRecord> detail(@PathVariable Long id) {
        return Result.ok(maintenanceService.getById(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    public Result<Void> save(@RequestParam Long deviceId,
                             @RequestParam String content,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate maintenanceDate,
                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nextMaintenanceDate,
                             @RequestParam(required = false) MultipartFile attachment) {
        MaintenanceRecord record = new MaintenanceRecord();
        record.setDeviceId(deviceId);
        record.setOperatorId(SessionUtil.getLoginUserId());
        record.setContent(content);
        record.setMaintenanceDate(maintenanceDate);
        record.setNextMaintenanceDate(nextMaintenanceDate);
        if (attachment != null && !attachment.isEmpty()) {
            record.setAttachmentName(fileStorageService.store(attachment));
        }
        maintenanceService.save(record);
        return Result.ok();
    }
}
