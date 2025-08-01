package com.vaultsearch.consumerservice.service;

import com.vaultsearch.consumerservice.model.FileDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenSearchService {

    private final OpenSearchClient openSearchClient;

    public void indexFileDocument(FileDocument document) {
        try {
            IndexRequest<FileDocument> request = new IndexRequest.Builder<FileDocument>()
                    .index("files") // The name of our index
                    .id(document.getFileId())
                    .document(document)
                    .build();
            openSearchClient.index(request);
            log.info("Successfully indexed document for fileId: {}", document.getFileId());
        } catch (IOException e) {
            log.error("Failed to index document with fileId: {}", document.getFileId(), e);
        }
    }
}