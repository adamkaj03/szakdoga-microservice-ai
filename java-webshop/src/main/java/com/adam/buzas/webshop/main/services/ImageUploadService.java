package com.adam.buzas.webshop.main.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class ImageUploadService {
    private static final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);
    private final String bucketName = "webshop-books-images";
    private final Region region = Region.EU_NORTH_1;

    public String uploadImage(MultipartFile file, String key) {
        try (S3Client s3 = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build()) {

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return "https://" + bucketName + ".s3." + region.id() + ".amazonaws.com/" + key;
        } catch (Exception e) {
            logger.error("Hiba a kép feltöltésekor:", e);
            return "";
        }
    }
}
