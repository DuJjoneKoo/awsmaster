package com.example.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    // S3 작업을 수행하는 클라이언트 (파일 업로드, 삭제 등)
    private final S3Client s3Client;

    // application-prod.yml 에서 버킷 이름을 가져옴
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file) throws IOException {
        // 파일 이름이 중복되지 않도록 UUID로 고유한 이름을 만듦
        // 예시: "550e8400-uuid_정민교.jpg"
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // S3에 파일을 업로드함
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)       // 어떤 버킷에 저장할지
                        .key(fileName)        // 파일 이름 (경로)
                        .contentType(file.getContentType()) // 파일 타입 (jpg, png 등)
                        .build(),
                RequestBody.fromBytes(file.getBytes())); // 실제 파일 데이터

        // 파일 key(이름)만 반환 (DB에 저장용)
        return fileName;
    }

    // Presigned URL 생성 (유효기간 7일)
    public String getPresignedUrl(String key) {
        S3Presigner presigner = S3Presigner.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(7))
                .getObjectRequest(r -> r.bucket(bucket).key(key))
                .build();

        return presigner.presignGetObject(presignRequest).url().toString();
    }
}
