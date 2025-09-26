package com.photo_lab.photo_lab.services;

import com.photo_lab.photo_lab.dtos.PhotoDTO;
import com.photo_lab.photo_lab.models.Photo;
import com.photo_lab.photo_lab.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;   // ✅ Correct package
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;


import java.net.URL;
import java.time.Duration;
import java.util.List;

@Service
public class PhotoService {
    private final PhotoRepository repository;
    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public PhotoService(PhotoRepository repository, S3Client s3Client, S3Presigner presigner) {
        this.repository = repository;
        this.s3Client = s3Client;
        this.presigner = presigner;
    }

    public void uploadPhoto(MultipartFile file, String description) throws Exception {
        String key = "photos/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build(),
                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
        );

        Photo photo = new Photo();
        photo.setS3Key(key);
        photo.setDescription(description);
        repository.save(photo);
    }

    public List<PhotoDTO> listPhotos() {
        return repository.findAll().stream().map(photo -> {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(photo.getS3Key())
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(2))
                    .getObjectRequest(getObjectRequest)
                    .build();

            URL url = presigner.presignGetObject(presignRequest).url();

            return new PhotoDTO(photo.getDescription(), url.toString());
        }).toList();
    }
}
