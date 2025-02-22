package com.chone.server.domains.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.domains.s3.exception.S3ExceptionCode;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;
  @Value("${cloud.aws.s3.bucket-name}")
  private String bucketName;

  public String uploadFile(MultipartFile file) {

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new ApiBusinessException(S3ExceptionCode.INVALID_FILE_TYPE);
    }

    String fileName = generateFileName(file.getOriginalFilename());
    try {
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentLength(file.getSize());

      amazonS3.putObject(
          new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
              .withCannedAcl(CannedAccessControlList.PublicRead));
    } catch (IOException e) {
      throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
    }

    return amazonS3.getUrl(bucketName, fileName).toString();
  }

  public void removeFile(String fileUrl) {

    if (fileUrl == null || fileUrl.isEmpty()) {
      return;
    }

    String fileKey = extractFileKey(fileUrl);
    amazonS3.deleteObject(bucketName, fileKey);
  }

  private String generateFileName(String name) {

    return UUID.randomUUID() + "_" + name;
  }

  private String extractFileKey(String fileUrl) {

    String[] parts = fileUrl.split("/");
    return parts[parts.length - 1];
  }
}