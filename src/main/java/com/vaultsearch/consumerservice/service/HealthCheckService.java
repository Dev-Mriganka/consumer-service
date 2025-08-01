package com.vaultsearch.consumerservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.opensearch.cluster.HealthResponse;
import org.opensearch.client.opensearch.indices.ExistsRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthCheckService implements CommandLineRunner {

    private final OpenSearchClient openSearchClient;

    @Override
    public void run(String... args) {
        try {
            log.info("🔍 Testing OpenSearch connection...");

            // Check cluster health
            HealthResponse health = openSearchClient.cluster().health();
            log.info("✅ OpenSearch cluster status: {}", health.status());
            log.info("✅ Number of nodes: {}", health.numberOfNodes());
            log.info("✅ Number of data nodes: {}", health.numberOfDataNodes());

            // Check if 'files' index exists
            boolean indexExists = openSearchClient.indices()
                    .exists(ExistsRequest.of(e -> e.index("files")))
                    .value();

            if (!indexExists) {
                log.info("📁 Creating 'files' index...");
                openSearchClient.indices().create(c -> c.index("files"));
                log.info("✅ 'files' index created successfully");
            } else {
                log.info("✅ 'files' index already exists");
            }

        } catch (Exception e) {
            log.error("❌ Failed to connect to OpenSearch", e);
        }
    }
}
