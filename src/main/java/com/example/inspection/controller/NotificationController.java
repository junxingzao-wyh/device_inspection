package com.example.inspection.controller;

import com.example.inspection.common.PageResult;
import com.example.inspection.common.Result;
import com.example.inspection.entity.Notification;
import com.example.inspection.service.NotificationService;
import com.example.inspection.util.SessionUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Result<PageResult<Notification>> list(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false) Integer isRead) {
        return Result.ok(notificationService.pageList(SessionUtil.getLoginUserId(), isRead, page, size));
    }

    @GetMapping("/unread-count")
    public Result<Integer> unreadCount() {
        return Result.ok(notificationService.countUnread(SessionUtil.getLoginUserId()));
    }

    @PutMapping("/{id}/read")
    public Result<Void> read(@PathVariable Long id) {
        notificationService.markRead(id, SessionUtil.getLoginUserId());
        return Result.ok();
    }

    @PutMapping("/read-all")
    public Result<Void> readAll() {
        notificationService.markAllRead(SessionUtil.getLoginUserId());
        return Result.ok();
    }
}
