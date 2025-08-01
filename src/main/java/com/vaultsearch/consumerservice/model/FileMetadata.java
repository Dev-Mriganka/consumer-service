package com.vaultsearch.consumerservice.model;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
@Data
public class FileMetadata {


    private String fileId;
    private String s3Key;
    private String fileName;
    private String contentType;
    private Instant uploadedAt;

    @DynamoDbPartitionKey
    public String getFileId() {
        return fileId;
    }

}

