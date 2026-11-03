package com.example.inspection.mapper;

import com.example.inspection.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<Notification> selectList(@Param("receiverId") Long receiverId,
                                  @Param("isRead") Integer isRead,
                                  @Param("offset") int offset,
                                  @Param("size") int size);

    long selectCount(@Param("receiverId") Long receiverId,
                     @Param("isRead") Integer isRead);

    int insert(Notification notification);

    int markReadById(@Param("id") Long id, @Param("receiverId") Long receiverId);

    int markAllRead(@Param("receiverId") Long receiverId);

    int countUnread(@Param("receiverId") Long receiverId);
}
