package com.example.dynamoDemo.repository;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
public abstract interface DynamoDbRepository<T> {

    <T> CompletableFuture<T> getConsistent(Key key, DynamoDbAsyncTable<T> table);

    <T> CompletableFuture<T> put(PutItemEnhancedRequest<T> request, DynamoDbAsyncTable<T> table);

    <T> CompletableFuture<T> put(T item, DynamoDbAsyncTable<T> table);

    <T> CompletableFuture<T> update(UpdateItemEnhancedRequest<T> request, DynamoDbAsyncTable<T> table);

    <T> CompletableFuture<T> update(T item, DynamoDbAsyncTable<T> table, Class<T> tableType);

    <T> DynamoDbAsyncTable<T> table(String tableName, Class<T> tableType);

    <T> SdkPublisher<Page<T>> getPage(DynamoDbAsyncTable<T> table, Map<String, AttributeValue> exclusiveStartKey, Integer limit);

    <T> PagePublisher<T> getAll(DynamoDbAsyncTable<T> table);
}
