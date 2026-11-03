package com.example.inspection.mapper;

import com.example.inspection.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectById(@Param("id") Long id);

    User selectByUsername(@Param("username") String username);

    int insert(User user);

    int updateById(User user);

    List<User> selectByRole(@Param("role") String role);

    /** 查询全部用户，用于启动时自动修复密码 */
    List<User> selectAll();
}
