package com.example.inspection.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Web 相关配置
 * 1. 将 /files/** 映射到本地上传目录
 * 2. 注册登录拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;

    /** 上传目录，来自 application.yml 的 app.upload-dir */
    @Value("${app.upload-dir}")
    private String uploadDir;

    public WebMvcConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 归一化路径，避免相对路径导致映射失败
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize();
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + dir + "/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/api/auth/login",
                        "/api/auth/logout",
                        "/files/**",
                        "/css/**",
                        "/js/**",
                        "/lib/**",
                        "/error/**",
                        "/favicon.ico"
                );
    }
}
