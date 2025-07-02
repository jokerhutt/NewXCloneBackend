package com.xclone.xclone.storage;

import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.List;

@Service
public class CloudStorageService {

    @Value("${gcs.bucket}")
    private String bucketName;

    public String upload(String fileName, InputStream inputStream, String contentType) {

        Storage storage = StorageOptions.getDefaultInstance().getService();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(contentType)
                .build();

        storage.create(blobInfo, inputStream);

        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
}