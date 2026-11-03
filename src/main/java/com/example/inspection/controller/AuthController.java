package com.example.inspection.controller;

import com.example.inspection.common.Result;
import com.example.inspection.config.LoginInterceptor;
import com.example.inspection.entity.User;
import com.example.inspection.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<User> login(@RequestParam String username,
                              @RequestParam String password,
                              HttpServletRequest request) {
        User user = userService.login(username, password);
        request.getSession().setAttribute(LoginInterceptor.SESSION_USER, user);
        // 返回时去掉密码
        user.setPassword(null);
        return Result.ok(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
        return Result.ok();
    }

    @GetMapping("/me")
    public Result<User> me(HttpServletRequest request) {
        Object u = request.getSession(false).getAttribute(LoginInterceptor.SESSION_USER);
        if (u == null) return Result.error(401, "未登录");
        User user = (User) u;
        user.setPassword(null);
        return Result.ok(user);
    }
}
