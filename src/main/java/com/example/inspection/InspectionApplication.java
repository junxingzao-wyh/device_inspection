package com.example.inspection;

import com.example.inspection.mapper.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.example.inspection.mapper")
public class InspectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspectionApplication.class, args);
    }

    /**
     * 密码自动修复：
     * 启动时检查所有 user 的 password 是否匹配明文 "123456"
     * 如果不匹配（明文/错误密文/NULL），自动生成正确的 BCrypt 密文写回数据库
     *
     * 效果：你永远只需要在登录页输入 123456，不需要再去 Navicat 手动改密文
     */
    @Bean
    public CommandLineRunner fixUserPasswords(UserMapper userMapper) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String correctHash = encoder.encode("123456");
            java.util.List<com.example.inspection.entity.User> all = userMapper.selectAll();
            int fixed = 0;
            for (com.example.inspection.entity.User u : all) {
                boolean needFix = u.getPassword() == null
                        || u.getPassword().isEmpty()
                        || u.getPassword().length() != 60
                        || !u.getPassword().startsWith("$2a$")
                        || !encoder.matches("123456", u.getPassword());
                if (needFix) {
                    u.setPassword(correctHash);
                    userMapper.updateById(u);
                    fixed++;
                }
            }
            System.out.println("========================================");
            System.out.println("[密码自动修复] 共 " + all.size() + " 个用户，修复了 " + fixed + " 个");
            System.out.println("[密码自动修复] 明文 '123456' 对应本次正确密文: " + correctHash);
            System.out.println("========================================");
        };
    }
}
