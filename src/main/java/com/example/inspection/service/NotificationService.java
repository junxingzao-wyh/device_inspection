package com.example.inspection.service;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Notification;

public interface NotificationService {
    PageResult<Notification> pageList(Long receiverId, Integer isRead, int page, int size);

    void markRead(Long id, Long receiverId);

    void markAllRead(Long receiverId);

    int countUnread(Long receiverId);
}
