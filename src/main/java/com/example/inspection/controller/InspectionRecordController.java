package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.InspectionRecord;
import com.example.inspection.service.InspectionService;
import com.example.inspection.util.FileStorageService;
import com.example.inspection.util.SessionUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/inspection-records")
public class InspectionRecordController {

    private final InspectionService inspectionService;
    private final FileStorageService fileStorageService;

    public InspectionRecordController(InspectionService inspectionService, FileStorageService fileStorageService) {
        this.inspectionService = inspectionService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public Result<PageResult<InspectionRecord>> list(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) Long deviceId,
                                                     @RequestParam(required = false) String result) {
        return Result.ok(inspectionService.recordPage(deviceId, result, page, size));
    }

    @GetMapping("/{id}")
    public Result<InspectionRecord> detail(@PathVariable Long id) {
        return Result.ok(inspectionService.getRecord(id));
    }

    @PostMapping(consumes = "multipart/form-data")
    public Result<Void> execute(@RequestParam(required = false) Long planId,
                                @RequestParam Long deviceId,
                                @RequestParam(defaultValue = "NORMAL") String result,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) @DateTimeFormat LocalDateTime inspectTime,
                                @RequestParam(required = false) MultipartFile photo) {
        InspectionRecord record = new InspectionRecord();
        record.setPlanId(planId);
        record.setDeviceId(deviceId);
        record.setInspectorId(SessionUtil.getLoginUserId());
        record.setResult(result);
        record.setDescription(description);
        record.setInspectTime(inspectTime == null ? LocalDateTime.now() : inspectTime);
        if (photo != null && !photo.isEmpty()) {
            record.setPhotoName(fileStorageService.store(photo));
        }
        inspectionService.execute(record);
        return Result.ok();
    }
}
