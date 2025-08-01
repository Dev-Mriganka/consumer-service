package com.vaultsearch.consumerservice.repository;


import com.vaultsearch.consumerservice.model.FileMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class FileMetadataRepository {

    private final DynamoDbTable<FileMetadata> fileMetadataTable;

    public FileMetadataRepository(DynamoDbEnhancedClient enhancedClient,
                                  @Value("${aws.dynamodb.table-name}") String tableName) {
        this.fileMetadataTable = enhancedClient.table(tableName, TableSchema.fromBean(FileMetadata.class));
    }

    public FileMetadata findById(String id) {
        return fileMetadataTable.getItem(r -> r.key(k -> k.partitionValue(id)));
    }
}
