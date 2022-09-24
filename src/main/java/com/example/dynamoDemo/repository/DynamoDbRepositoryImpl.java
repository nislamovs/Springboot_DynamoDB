package com.example.dynamoDemo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class DynamoDbRepositoryImpl<T> implements DynamoDbRepository<T> {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;

    @Override
    public <T> CompletableFuture<T> getConsistent(Key key, DynamoDbAsyncTable<T> table) {

        final GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                .key(key)
                .consistentRead(true)
                .build();
        return table.getItem(request);
    }

    @Override
    public <T> CompletableFuture<T> put(PutItemEnhancedRequest<T> request, DynamoDbAsyncTable<T> table) {

        return (CompletableFuture<T>) table.putItem(request);
    }

    @Override
    public <T> CompletableFuture<T> put(T item, DynamoDbAsyncTable<T> table) {

        return (CompletableFuture<T>) table.putItem(item);
    }

    @Override
    public <T> CompletableFuture<T> update(UpdateItemEnhancedRequest<T> request, DynamoDbAsyncTable<T> table) {

        return table.updateItem(request);
    }

    @Override
    public <T> CompletableFuture<T> update(T item, DynamoDbAsyncTable<T> table, Class<T> tableType) {

        final UpdateItemEnhancedRequest<T> updateRequest = UpdateItemEnhancedRequest.builder(tableType)
                .item(item)
                .ignoreNulls(true)
                .build();
        return table.updateItem(updateRequest);
    }

    @Override
    public <T> DynamoDbAsyncTable<T> table(String tableName, Class<T> tableType) {

        return enhancedAsyncClient.table(tableName, TableSchema.fromImmutableClass(tableType));
    }


    @Override
    public <T> SdkPublisher<Page<T>> getPage(DynamoDbAsyncTable<T> table, Map<String, AttributeValue> exclusiveStartKey,
                                             Integer limit) {

        final ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .exclusiveStartKey(exclusiveStartKey)
                .limit(limit)
                .build();

        return table.scan(request).limit(1);
    }

    @Override
    public <T> PagePublisher<T> getAll(DynamoDbAsyncTable<T> table) {

        return table.scan();
    }
}
