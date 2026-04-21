package com.tour.controller;

import com.tour.common.R;
import com.tour.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器 —— 本地文件存储，后续可扩展为 OSS
 */
@Tag(name = "文件上传", description = "图片/视频等文件上传接口")
@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {

    @Autowired
    private StorageService storageService;

    /** 上传文件（需要登录），返回文件名和访问 URL */
    @Operation(summary = "上传文件", description = "上传图片或视频，返回文件名（filename）及访问地址（url）")
    @PostMapping("/upload")
    public R<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String filename = storageService.store(file);
        String url = storageService.getUrl(filename);
        Map<String, String> result = new HashMap<>();
        result.put("filename", filename);
        result.put("url", url);
        return R.success(result);
    }
}
