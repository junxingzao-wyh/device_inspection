package com.example.inspection.mapper;

import com.example.inspection.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    Device selectById(@Param("id") Long id);

    List<Device> selectList(@Param("keyword") String keyword,
                            @Param("status") String status,
                            @Param("offset") int offset,
                            @Param("size") int size);

    long selectCount(@Param("keyword") String keyword,
                     @Param("status") String status);

    int insert(Device device);

    int updateById(Device device);
}
