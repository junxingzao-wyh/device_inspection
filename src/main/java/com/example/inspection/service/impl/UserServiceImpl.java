package com.example.inspection.service.impl;

import com.example.inspection.common.BusinessException;
import com.example.inspection.entity.User;
import com.example.inspection.mapper.UserMapper;
import com.example.inspection.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    @Override
    public User login(String username, String rawPassword) {
        User user = userMapper.selectByUsername(username);
        if (user == null) throw new BusinessException("用户名或密码错误");
        if (user.getStatus() != 1) throw new BusinessException("账号已停用");
        if (!encoder.matches(rawPassword, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public List<User> listByRole(String role) {
        return userMapper.selectByRole(role);
    }

    @Override
    public void update(User user) {
        userMapper.updateById(user);
    }
}
