package com.adam.buzas.webshop.main.services;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageUploadService {
    private final String storageConnectionString = "";

    public String uploadImage(MultipartFile file, String blobName) {
        try {
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            // Azure Blob Storage tároló nevének megadása
            CloudBlobContainer container = blobClient.getContainerReference("kepek");

            //kép neve lesz a blob azonosító
            CloudBlockBlob blob = container.getBlockBlobReference(blobName);
            blob.upload(file.getInputStream(), file.getSize());
            return blob.getUri().toString();
        } catch (Exception e) {

        }
        return "";
    }
}
