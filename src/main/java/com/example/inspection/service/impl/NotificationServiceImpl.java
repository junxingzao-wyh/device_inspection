package com.example.inspection.service.impl;

import com.example.inspection.common.PageResult;
import com.example.inspection.entity.Notification;
import com.example.inspection.mapper.NotificationMapper;
import com.example.inspection.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper mapper;

    public NotificationServiceImpl(NotificationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PageResult<Notification> pageList(Long receiverId, Integer isRead, int page, int size) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;
        List<Notification> list = mapper.selectList(receiverId, isRead, offset, size);
        long total = mapper.selectCount(receiverId, isRead);
        return PageResult.of(list, total, page, size);
    }

    @Override
    public void markRead(Long id, Long receiverId) {
        mapper.markReadById(id, receiverId);
    }

    @Override
    public void markAllRead(Long receiverId) {
        mapper.markAllRead(receiverId);
    }

    @Override
    public int countUnread(Long receiverId) {
        int n = mapper.countUnread(receiverId);
        return n < 0 ? 0 : n;
    }
}
