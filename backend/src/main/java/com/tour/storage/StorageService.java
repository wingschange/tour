package com.tour.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    /**
     * Store an uploaded file and return its stored filename.
     */
    String store(MultipartFile file);

    /**
     * Delete a stored file by its filename.
     */
    void delete(String filename);

    /**
     * Get the accessible URL for a stored filename.
     */
    String getUrl(String filename);
}
