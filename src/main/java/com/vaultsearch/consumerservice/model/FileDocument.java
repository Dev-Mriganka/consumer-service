package com.vaultsearch.consumerservice.model;

import lombok.Data;
import java.time.Instant;

@Data
public class FileDocument {
    private String fileId;
    private String s3Key;
    private String fileName;
    private String contentType;
    private Instant uploadedAt;
}