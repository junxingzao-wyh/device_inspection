package com.example.inspection.mapper;

import com.example.inspection.entity.InspectionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InspectionRecordMapper {
    InspectionRecord selectById(@Param("id") Long id);

    List<InspectionRecord> selectList(@Param("deviceId") Long deviceId,
                                      @Param("result") String result,
                                      @Param("offset") int offset,
                                      @Param("size") int size);

    long selectCount(@Param("deviceId") Long deviceId,
                     @Param("result") String result);

    int insert(InspectionRecord record);
}
