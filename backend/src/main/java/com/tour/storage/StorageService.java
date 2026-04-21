package com.tour.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口
 *
 * <p>当前实现：本地磁盘存储（{@code LocalStorageServiceImpl}）。
 * 未来可替换为阿里云 OSS 或其他对象存储服务。</p>
 */
public interface StorageService {

    /**
     * 存储上传的文件，返回存储后的文件名（UUID 格式）
     *
     * @param file 上传的文件
     * @return 文件名（如 {@code 550e8400-e29b-41d4-a716-446655440000.jpg}）
     */
    String store(MultipartFile file);

    /**
     * 根据文件名删除已存储的文件
     *
     * @param filename 文件名
     */
    void delete(String filename);

    /**
     * 根据文件名获取可访问的 URL
     *
     * @param filename 文件名
     * @return 可访问的 URL 字符串
     */
    String getUrl(String filename);
}
