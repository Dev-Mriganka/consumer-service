package com.vaultsearch.consumerservice.service;

import com.vaultsearch.consumerservice.model.FileDocument;
import com.vaultsearch.consumerservice.model.FileMetadata;
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
    private final OpenSearchService openSearchService; // Correctly inject the service

    @SqsListener("${aws.sqs.queue-name}")
    public void listen(Map<String, String> message) {
        String fileId = message.get("fileId");
        log.info("Received message for fileId: {}", fileId);

        FileMetadata metadata = metadataRepository.findById(fileId);
        if (metadata == null) {
            log.error("No metadata found for fileId: {}", fileId);
            return;
        }

        FileDocument document = new FileDocument();
        document.setFileId(metadata.getFileId());
        document.setS3Key(metadata.getS3Key());
        document.setFileName(metadata.getFileName());
        document.setContentType(metadata.getContentType());
        document.setUploadedAt(metadata.getUploadedAt());

        openSearchService.indexFileDocument(document);
    }
}