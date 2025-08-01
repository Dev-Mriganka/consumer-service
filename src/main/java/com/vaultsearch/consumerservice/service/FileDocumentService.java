package com.vaultsearch.consumerservice.service;

import lombok.RequiredArgsConstructor;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch.core.IndexRequest;
import org.opensearch.client.opensearch.core.IndexResponse;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.SearchResponse;
import org.opensearch.client.opensearch.core.search.Hit;
import org.opensearch.client.opensearch._types.query_dsl.MatchQuery;
import com.vaultsearch.consumerservice.model.FileDocument;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileDocumentService {

    private final OpenSearchClient openSearchClient;

    public String indexFile(FileDocument fileDocument) throws IOException {
        IndexRequest<FileDocument> request = IndexRequest.of(i -> i
                .index("file_documents")
                .id(fileDocument.getFileId())
                .document(fileDocument)
        );
        IndexResponse response = openSearchClient.index(request);
        return response.id();
    }

    public List<FileDocument> searchByName(String name) throws IOException {
        SearchRequest request = SearchRequest.of(s -> s
                .index("file_documents")
                .query(q -> q
                        .match(m -> m
                                .field("fileName")
                                .query(FieldValue.of(name))
                        )
                )
        );

        SearchResponse<FileDocument> response = openSearchClient.search(request, FileDocument.class);
        return response.hits().hits().stream().map(Hit::source).toList();
    }
}
