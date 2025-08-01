package com.vaultsearch.consumerservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

@Document(indexName = "files") // The name of the index in OpenSearch
@Data
public class FileDocument {

    @Id
    private String fileId;

    @Field(type = FieldType.Keyword, name = "s3Key") // Add this field
    private String s3Key;

    @Field(type = FieldType.Text, name = "fileName")
    private String fileName;

    @Field(type = FieldType.Keyword, name = "contentType")
    private String contentType;

    @Field(type = FieldType.Date, name = "uploadedAt")
    private Instant uploadedAt;
}
