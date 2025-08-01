package com.vaultsearch.consumerservice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.vaultsearch.consumerservice.repository")
@Slf4j
public class OpenSearchConfig {

    @Value("${spring.elasticsearch.uris}")
    private String opensearchUrl;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    // 1. Create the low-level RestClient
    @Bean
    public RestClient restClient() {
        try {
            log.info("Creating RestClient for OpenSearch at: {}", opensearchUrl);

            final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));

            RestClient client = RestClient.builder(HttpHost.create(opensearchUrl))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder
                                    .setDefaultCredentialsProvider(credentialsProvider)
                                    .setMaxConnTotal(100)
                                    .setMaxConnPerRoute(30))
                    .build();

            log.info("RestClient created successfully");
            return client;
        } catch (Exception e) {
            log.error("Failed to create RestClient", e);
            throw e;
        }
    }

    // 2. Create the transport layer
    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    // 3. Create the modern ElasticsearchClient
    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }

    // 4. Create the ElasticsearchOperations bean using the correct template class
    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchTemplate(elasticsearchClient);
    }
}