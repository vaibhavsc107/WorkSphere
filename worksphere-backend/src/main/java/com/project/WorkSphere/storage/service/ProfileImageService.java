package com.project.WorkSphere.storage.service;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileImageService {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    private static final Set<String> ALLOWED_TYPES =
            Set.of("image/jpeg", "image/png", "image/webp");

    private final Path uploadDirectory;

    public ProfileImageService(
            @Value("${app.upload.profile-dir}") String uploadDir) {

        this.uploadDirectory = Paths.get(uploadDir)
                .toAbsolutePath()
                .normalize();
    }
    
    private void validateImageContent(MultipartFile file) {

        try {
            byte[] imageBytes = file.getBytes();

            ImageInfo imageInfo = Imaging.getImageInfo(imageBytes);
                    
       
            if (imageInfo == null) {
                throw new IllegalArgumentException(
                        "Uploaded file is not a valid image"
                );
            }

            String detectedFormat =
                    imageInfo.getFormat().getName().toLowerCase();

            boolean validFormat = switch (detectedFormat) {
                case "jpeg", "jpg", "png", "webp" -> true;
                default -> false;
            };

            if (!validFormat) {
                throw new IllegalArgumentException(
                        "Only JPEG, PNG and WebP images are allowed"
                );
            }

        } catch (IllegalArgumentException exception) {
            throw exception;

        } catch (Exception exception) {
            throw new IllegalArgumentException(
                    "Invalid or corrupted image file",
                    exception
            );
        }
    }
    
    public void deleteProfileImage(String filename) {

        if (filename == null || filename.isBlank()) {
            return;
        }

        if (!filename.matches(
                "^[a-fA-F0-9-]{36}\\.(jpg|jpeg|png|webp)$")) {
            throw new IllegalArgumentException(
                    "Invalid profile image filename"
            );
        }

        try {
            Path filePath = uploadDirectory.resolve(filename).normalize();

            if (!filePath.getParent().equals(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.deleteIfExists(filePath);

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to delete previous profile image",
                    exception
            );
        }
    }
    
    public Resource loadProfileImage(String filename) {

        if (filename == null || filename.isBlank()
                || !filename.matches(
                    "^[a-fA-F0-9-]{36}\\.(jpg|jpeg|png|webp)$")) {

            throw new IllegalArgumentException(
                    "Invalid profile image filename"
            );
        }

        try {
            Path filePath = uploadDirectory.resolve(filename).normalize();

            if (!filePath.getParent().equals(uploadDirectory)
                    || !Files.isRegularFile(filePath)) {

                throw new IllegalArgumentException(
                        "Profile image not found"
                );
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException(
                        "Profile image is not readable"
                );
            }

            return resource;

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to retrieve profile image", exception
            );
        }
    }

    public String uploadProfileImage(MultipartFile file) {

        validateFile(file);

        try {
            Files.createDirectories(uploadDirectory);

            String originalFilename = file.getOriginalFilename();

            String extension = getExtension(originalFilename);

            String filename = UUID.randomUUID() + extension;

            Path targetPath = uploadDirectory.resolve(filename)
                    .normalize();

            if (!targetPath.getParent().equals(uploadDirectory)) {
                throw new IllegalArgumentException("Invalid file path");
            }

            Files.copy(file.getInputStream(), targetPath);

            return filename;

        } catch (IOException exception) {
            throw new RuntimeException(
                    "Unable to upload profile image", exception
            );
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please select a profile image"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    "Profile image must not exceed 2 MB"
            );
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException(
                    "Only JPEG, PNG and WebP images are allowed"
            );
        }

        String extension = getExtension(file.getOriginalFilename());
        
        

        if (!Set.of(".jpg", ".jpeg", ".png", ".webp")
                .contains(extension)) {
            throw new IllegalArgumentException(
                    "Invalid image file extension"
            );
        }
        
        validateImageContent(file);
    }

    private String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            throw new IllegalArgumentException(
                    "Invalid image filename"
            );
        }

        return filename.substring(filename.lastIndexOf("."))
                .toLowerCase();
    }
}
