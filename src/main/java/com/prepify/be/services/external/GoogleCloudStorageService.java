package com.prepify.be.services.external;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;;

@Service
public class GoogleCloudStorageService {
    @Value("${spring.google.storage.project.id}")
    private String gcsProjectId;

    @Value("${spring.google.storage.bucket.name}")
    private String gcsbucketName;

    @Value("${spring.public.google.storage.host}")
    private String ggcStoragePublicHost;

    public String uploadFile(MultipartFile file) throws IOException {
        Credentials credentials;

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("prepify-be-cloudstorage.json")) {
            credentials = GoogleCredentials.fromStream(inputStream);
        }

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId(gcsProjectId).build().getService();
        String bucketName = gcsbucketName;
        String fileName = file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);

        // Set content type based on file extension or MIME type
        String contentType = file.getContentType();

        // Set content disposition to "inline" to make the file viewable in the browser
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .setContentDisposition("inline; filename=\"" + fileName + "\"")
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes());

        return ggcStoragePublicHost + "/" + blob.getBucket() + "/" + blob.getName();
    }
}
