package com.example.inspection.service;

import com.example.inspection.entity.User;

import java.util.List;

public interface UserService {
    /** 返回登录用户，不存在返回 null */
    User findByUsername(String username);

    /** 验证登录：密码正确返回用户，否则抛异常 */
    User login(String username, String rawPassword);

    User getById(Long id);

    List<User> listByRole(String role);

    void update(User user);
}
