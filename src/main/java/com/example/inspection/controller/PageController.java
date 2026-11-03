package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.InspectionPlan;
import com.example.inspection.entity.RepairOrder;
import com.example.inspection.entity.User;
import com.example.inspection.service.InspectionService;
import com.example.inspection.service.NotificationService;
import com.example.inspection.service.RepairOrderService;
import com.example.inspection.service.UserService;
import com.example.inspection.util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面控制器：返回 FreeMarker 模板与 JSP 登录页
 */
@Controller
public class PageController {

    private final InspectionService inspectionService;
    private final RepairOrderService repairOrderService;
    private final NotificationService notificationService;
    private final UserService userService;

    public PageController(InspectionService inspectionService,
                          RepairOrderService repairOrderService,
                          NotificationService notificationService,
                          UserService userService) {
        this.inspectionService = inspectionService;
        this.repairOrderService = repairOrderService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    /** JSP 登录页；拦截器已放行 /login */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ===== 首页 =====
    @GetMapping("/")
    public String index(Model model) {
        PageResult<RepairOrder> pending = repairOrderService.pageList("PENDING_ASSIGN", null, null, 1, 1);
        PageResult<RepairOrder> processing = repairOrderService.pageList("PROCESSING", null, null, 1, 1);
        PageResult<InspectionPlan> plans = inspectionService.planPage(null, 1, 1);
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingOrderCount", pending.getTotal());
        stats.put("processingOrderCount", processing.getTotal());
        stats.put("planCount", plans.getTotal());
        model.addAttribute("stats", stats);
        model.addAttribute("unreadCount", notificationService.countUnread(SessionUtil.getLoginUserId()));
        return "index";
    }

    // ===== 设备 =====
    @GetMapping("/device/list")
    public String deviceList() { return "device/list"; }

    @GetMapping("/device/form")
    public String deviceForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("id", id);
        return "device/form";
    }

    @GetMapping("/device/detail")
    public String deviceDetail(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        return "device/detail";
    }

    // ===== 巡检 =====
    @GetMapping("/inspection/plan")
    public String planList() { return "inspection/plan_list"; }

    @GetMapping("/inspection/record")
    public String recordList() { return "inspection/record_list"; }

    @GetMapping("/inspection/execute")
    public String execute(@RequestParam(required = false) Long deviceId, Model model) {
        model.addAttribute("deviceId", deviceId);
        return "inspection/execute";
    }

    // ===== 工单 =====
    @GetMapping("/repair/list")
    public String repairList() { return "repair/list"; }

    @GetMapping("/repair/detail")
    public String repairDetail(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        return "repair/detail";
    }

    @GetMapping("/repair/form")
    public String repairForm(@RequestParam(required = false) Long deviceId, Model model) {
        model.addAttribute("deviceId", deviceId);
        return "repair/form";
    }

    // ===== 维保 =====
    @GetMapping("/maintenance/list")
    public String maintenanceList() { return "maintenance/list"; }

    @GetMapping("/maintenance/form")
    public String maintenanceForm(@RequestParam(required = false) Long deviceId, Model model) {
        model.addAttribute("deviceId", deviceId);
        return "maintenance/form";
    }

    // ===== 通知 =====
    @GetMapping("/notification/list")
    public String notificationList() { return "notification/list"; }

    // ===== 个人中心 =====
    @GetMapping("/user/profile")
    public String profile() { return "user/profile"; }

    // ===== 下拉数据（维修员/巡检员列表）=====
    @GetMapping("/common/repairers")
    @ResponseBody
    public List<User> repairers() { return userService.listByRole("REPAIRER"); }

    @GetMapping("/common/inspectors")
    @ResponseBody
    public List<User> inspectors() { return userService.listByRole("INSPECTOR"); }
}
