package com.tour.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 本地文件存储服务实现类
 *
 * <p>将上传的文件保存到本地磁盘，文件名使用 UUID 避免冲突。
 * 启动时会自动创建上传目录，存储路径通过 {@code file.upload-path} 配置。
 * 后续可替换为阿里云 OSS 等对象存储实现。</p>
 */
@Service
public class LocalStorageServiceImpl implements StorageService {

    /** 文件上传根目录，默认当前项目下的 uploads 文件夹 */
    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    /** 解析后的本地路径 */
    private Path rootLocation;

    /** 初始化上传目录 */
    @PostConstruct
    public void init() {
        rootLocation = Paths.get(uploadPath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("无法初始化文件存储目录: " + uploadPath, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("不能上传空文件");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            // 去除路径信息，防止目录遍历
            String baseName = new java.io.File(originalFilename).getName();
            int dotIndex = baseName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = baseName.substring(dotIndex).replaceAll("[^a-zA-Z0-9.]", "");
            }
        }
        String storedFilename = UUID.randomUUID().toString() + extension;
        try {
            Path destination = rootLocation.toAbsolutePath().normalize().resolve(storedFilename).normalize();
            // 二次校验，防止文件写出到上传目录之外
            if (!destination.startsWith(rootLocation.toAbsolutePath().normalize())) {
                throw new IllegalArgumentException("不能将文件存储到当前目录之外");
            }
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("文件存储失败: " + storedFilename, e);
        }
        return storedFilename;
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败: " + filename, e);
        }
    }

    @Override
    public String getUrl(String filename) {
        return "/files/" + filename;
    }
}
