package com.example.inspection.mapper;

import com.example.inspection.entity.InspectionPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InspectionPlanMapper {
    InspectionPlan selectById(@Param("id") Long id);

    List<InspectionPlan> selectList(@Param("deviceId") Long deviceId,
                                    @Param("offset") int offset,
                                    @Param("size") int size);

    long selectCount(@Param("deviceId") Long deviceId);

    int insert(InspectionPlan plan);

    int updateById(InspectionPlan plan);
}
