package com.vaultsearch.consumerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.providers.AwsRegionProvider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class AwsConfig {

    // The SqsClient and DynamoDbClient beans are autoconfigured by the starters

    @Bean
    public DynamoDbClient dynamoDbClient(AwsRegionProvider awsRegionProvider,
                                         AwsCredentialsProvider awsCredentialsProvider) {
        return DynamoDbClient.builder()
                .region(awsRegionProvider.getRegion())
                .credentialsProvider(awsCredentialsProvider)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}