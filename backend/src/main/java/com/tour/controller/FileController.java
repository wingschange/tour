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

@Tag(name = "File", description = "File upload endpoints")
@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {

    @Autowired
    private StorageService storageService;

    @Operation(summary = "Upload a file")
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
