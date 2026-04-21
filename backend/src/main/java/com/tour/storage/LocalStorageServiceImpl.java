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

@Service
public class LocalStorageServiceImpl implements StorageService {

    @Value("${file.upload-path:./uploads}")
    private String uploadPath;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        rootLocation = Paths.get(uploadPath);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location: " + uploadPath, e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file");
        }
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            // Strip any path separators to prevent directory traversal via filename
            String baseName = new java.io.File(originalFilename).getName();
            int dotIndex = baseName.lastIndexOf('.');
            if (dotIndex >= 0) {
                extension = baseName.substring(dotIndex).replaceAll("[^a-zA-Z0-9.]", "");
            }
        }
        String storedFilename = UUID.randomUUID().toString() + extension;
        try {
            Path destination = rootLocation.toAbsolutePath().normalize().resolve(storedFilename).normalize();
            if (!destination.startsWith(rootLocation.toAbsolutePath().normalize())) {
                throw new IllegalArgumentException("Cannot store file outside current directory");
            }
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + storedFilename, e);
        }
        return storedFilename;
    }

    @Override
    public void delete(String filename) {
        try {
            Path file = rootLocation.resolve(filename).normalize();
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + filename, e);
        }
    }

    @Override
    public String getUrl(String filename) {
        return "/files/" + filename;
    }
}
