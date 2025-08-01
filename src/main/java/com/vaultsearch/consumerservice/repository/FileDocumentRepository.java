package com.vaultsearch.consumerservice.repository;

import com.vaultsearch.consumerservice.model.FileDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileDocumentRepository extends ElasticsearchRepository<FileDocument, String> {
}
