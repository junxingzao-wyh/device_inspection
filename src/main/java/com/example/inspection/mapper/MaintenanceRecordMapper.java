package com.example.inspection.mapper;

import com.example.inspection.entity.MaintenanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaintenanceRecordMapper {
    MaintenanceRecord selectById(@Param("id") Long id);

    List<MaintenanceRecord> selectList(@Param("deviceId") Long deviceId,
                                       @Param("offset") int offset,
                                       @Param("size") int size);

    long selectCount(@Param("deviceId") Long deviceId);

    int insert(MaintenanceRecord record);
}
