package com.example.inspection.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

/**
 * 本地文件上传服务
 */
@Service
public class FileStorageService {

    /** 白名单后缀 */
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "pdf", "gif");
    /** 最大 10MB */
    private static final long MAX_SIZE = 10L * 1024 * 1024;

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload-dir}") String uploadDir) throws IOException {
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        if (!Files.exists(this.uploadDir)) {
            Files.createDirectories(this.uploadDir);
        }
    }

    /**
     * 保存文件：UUID 重命名 + 后缀校验
     * @return 存储文件名（不含路径）
     */
    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String original = file.getOriginalFilename();
        if (original == null) {
            throw new RuntimeException("原始文件名不存在");
        }
        String suffix = original.substring(original.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOWED.contains(suffix)) {
            throw new RuntimeException("文件类型不允许：" + suffix);
        }
        if (file.getSize() > MAX_SIZE) {
            throw new RuntimeException("文件超过 10MB 上限");
        }

        String newName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;
        Path target = uploadDir.resolve(newName).normalize();
        // 防止路径逃逸
        if (!target.startsWith(uploadDir)) {
            throw new RuntimeException("非法路径");
        }
        try {
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new RuntimeException("保存文件失败：" + e.getMessage(), e);
        }
        return newName;
    }
}
