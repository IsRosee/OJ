package com.yz.oj.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.access-url-prefix}")
    private String accessURLPrefix;

    public String storeFile(MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the file path
        Path filePath = Paths.get(uploadDir, fileName);

        // Save the file
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());


        return Filename2URL(fileName);
    }

    // 上传到${file.upload-dir}的文件将映射到/file/
    public String Filename2URL(String fileName) {
        return accessURLPrefix + fileName;
    }

    public String URL2Filename(String url) {
        return url.substring(accessURLPrefix.length());
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Files.deleteIfExists(filePath);
    }

    public Path loadFile(String fileName) {
        return Paths.get(uploadDir).resolve(fileName);
    }
}
