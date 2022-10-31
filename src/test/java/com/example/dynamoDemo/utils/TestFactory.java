package com.example.dynamoDemo.utils;

import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

public class TestFactory {

    public static final String PRODUCT_CATALOG_TABLENAME = "ProductCatalog";
    public static final String CUSTOMERS_TABLENAME = "Customers";


    public static CompletableFuture<CreateTableResponse> createProductCatalogTableAsync(DynamoDbAsyncClient asyncClient) {
        return asyncClient.createTable(CreateTableRequest.builder()
                .keySchema(
                        KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("Id")
                                .build(),
                        KeySchemaElement.builder()
                                .keyType(KeyType.RANGE)
                                .attributeName("ProductCategory")
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("Id")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("ProductCategory")
                                .attributeType(ScalarAttributeType.S)
                                .build()
                )
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(100L)
                                .writeCapacityUnits(100L)
                                .build())
                .tableName("ProductCatalog")
                .build()
        );
    }

    public static CompletableFuture<CreateTableResponse> createCustomersTableAsync(DynamoDbAsyncClient asyncClient) {
        return asyncClient.createTable(CreateTableRequest.builder()
                .keySchema(
                        KeySchemaElement.builder()
                                .keyType(KeyType.HASH)
                                .attributeName("Id")
                                .build(),
                        KeySchemaElement.builder()
                                .keyType(KeyType.RANGE)
                                .attributeName("Email")
                                .build()
                )
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName("Id")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("Email")
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName("Country")
                                .attributeType(ScalarAttributeType.S)
                                .build()

                )
                .globalSecondaryIndexes(GlobalSecondaryIndex.builder()
                        .indexName("Country-Index")
                        .projection(Projection.builder()
                                .projectionType(ProjectionType.ALL)
                                .build())
                        .keySchema(
                                KeySchemaElement.builder()
                                        .attributeName("Country")
                                        .keyType("HASH")
                                        .build(),
                                KeySchemaElement.builder()
                                        .attributeName("Email")
                                        .keyType("RANGE")
                                        .build()
                        )
                        .provisionedThroughput(
                                ProvisionedThroughput.builder()
                                        .readCapacityUnits(100L)
                                        .writeCapacityUnits(100L)
                                        .build())
                        .build())
                .provisionedThroughput(
                        ProvisionedThroughput.builder()
                                .readCapacityUnits(100L)
                                .writeCapacityUnits(100L)
                                .build())
                .tableName("Customers")
                .build()
        );
    }
}
