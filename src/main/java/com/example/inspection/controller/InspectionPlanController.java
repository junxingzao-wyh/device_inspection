package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.InspectionPlan;
import com.example.inspection.service.InspectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inspection-plans")
public class InspectionPlanController {

    private final InspectionService inspectionService;

    public InspectionPlanController(InspectionService inspectionService) {
        this.inspectionService = inspectionService;
    }

    @GetMapping
    public Result<PageResult<InspectionPlan>> list(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size,
                                                   @RequestParam(required = false) Long deviceId) {
        return Result.ok(inspectionService.planPage(deviceId, page, size));
    }

    @GetMapping("/{id}")
    public Result<InspectionPlan> detail(@PathVariable Long id) {
        return Result.ok(inspectionService.getPlan(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody InspectionPlan plan) {
        inspectionService.savePlan(plan);
        return Result.ok();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody InspectionPlan plan) {
        plan.setId(id);
        inspectionService.updatePlan(plan);
        return Result.ok();
    }
}
