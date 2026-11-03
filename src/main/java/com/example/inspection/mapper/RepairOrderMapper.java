package com.example.inspection.mapper;

import com.example.inspection.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RepairOrderMapper {
    RepairOrder selectById(@Param("id") Long id);

    RepairOrder selectByOrderNo(@Param("orderNo") String orderNo);

    List<RepairOrder> selectList(@Param("status") String status,
                                 @Param("keyword") String keyword,
                                 @Param("deviceId") Long deviceId,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    long selectCount(@Param("status") String status,
                     @Param("keyword") String keyword,
                     @Param("deviceId") Long deviceId);

    int insert(RepairOrder order);

    int updateById(RepairOrder order);

    String selectMaxOrderNo();
}
