package com.photo_lab.photo_lab.models;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String s3Key;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, updatable = false)
    private Instant uploadedAt = Instant.now();

    public Photo() {}

    public Photo(String s3Key, String description) {
        this.s3Key = s3Key;
        this.description = description;
        this.uploadedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", s3Key='" + s3Key + '\'' +
                ", description='" + description + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
