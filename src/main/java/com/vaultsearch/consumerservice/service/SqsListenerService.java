package com.vaultsearch.consumerservice.service;


import com.vaultsearch.consumerservice.model.FileDocument;
import com.vaultsearch.consumerservice.model.FileMetadata;
import com.vaultsearch.consumerservice.repository.FileDocumentRepository;
import com.vaultsearch.consumerservice.repository.FileMetadataRepository;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqsListenerService {

    private final FileMetadataRepository metadataRepository;
    private final FileDocumentRepository documentRepository;

    @SqsListener("${aws.sqs.queue-name}") // Listens to the queue specified in application.yml
    public void listen(Map<String, String> message) {
        String fileId = message.get("fileId");
        log.info("Received message for fileId: {}", fileId);

        // 1. Fetch metadata from DynamoDB
        FileMetadata metadata = metadataRepository.findById(fileId);
        if (metadata == null) {
            log.error("No metadata found for fileId: {}", fileId);
            return;
        }

        // 2. Create the document to be indexed
        FileDocument document = new FileDocument();
        document.setFileId(metadata.getFileId());
        document.setS3Key(metadata.getS3Key());
        document.setFileName(metadata.getFileName());
        document.setContentType(metadata.getContentType());
        document.setUploadedAt(metadata.getUploadedAt());

        // 3. Save the document to OpenSearch
        documentRepository.save(document);
        log.info("Successfully indexed document for fileId: {}", fileId);
    }
}