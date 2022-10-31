package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.netty.util.internal.StringUtil.isNullOrEmpty;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private DynamoDbAsyncTable<Product> productTable;
    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private final DynamoDbAsyncClient dynamoDbClient;

    @PostConstruct
    void init() {
        productTable = enhancedAsyncClient.table("ProductCategory", TableSchema.fromBean(Product.class));
    }

    //Pagination
    public SdkPublisher<Page<Product>> findAll(String lastId, Integer limit) {

        Map<String, AttributeValue> exclusiveStartKey = !isNullOrEmpty(lastId)
                ? Map.of("Id", AttributeValue.builder().s(lastId).build())
                : null;

        var itemsPerPage = ( limit == null || limit <= 0 || limit > 20 ) ? 20 : limit;

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .exclusiveStartKey(exclusiveStartKey)
                .limit(itemsPerPage)
                .build();

        return productTable.scan(request).limit(1);
    }

    public PagePublisher<Product> findAll() {
        return productTable.scan();
    }

    //Find item
    public CompletableFuture<Product> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                .key(key)
                .consistentRead(true)
                .build();

        return productTable.getItem(request);
    }

    public PagePublisher<Product> findByProductCategory(String productCategory) {

        AttributeValue att = AttributeValue.builder()
                .s(productCategory)
                .build();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":productCategory", att);

        Expression expression = Expression.builder()
                .expression("ProductCategory = :productCategory")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .filterExpression(expression)
                .build();

        return productTable.scan(request);
    }

//Count

    public long countDbItems() throws ExecutionException, InterruptedException {
        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(productTable.tableName())
                .build();

        TableDescription tableInfo = dynamoDbClient.describeTable(request).get().table();
        return tableInfo.itemCount();
    }

    //Save
    public CompletableFuture<Void> save(Product product) {
        PutItemEnhancedRequest<Product> request = PutItemEnhancedRequest.builder(Product.class)
                .item(product)
                .build();

        return productTable.putItem(request);
    }

    public CompletableFuture<Void> save(Product product, Expression conditionExpression) {
        PutItemEnhancedRequest<Product> request = PutItemEnhancedRequest.builder(Product.class)
                .conditionExpression(conditionExpression)
                .item(product)
                .build();

        return productTable.putItem(request);
    }

    //Update
    public CompletableFuture<Product> update(Product product) {
        UpdateItemEnhancedRequest<Product> request = UpdateItemEnhancedRequest.builder(Product.class)
                .item(product)
                .ignoreNulls(true)
                .build();

        return productTable.updateItem(request);
    }

    public CompletableFuture<Product> update(Product product, Expression conditionExpression) {
        UpdateItemEnhancedRequest<Product> request = UpdateItemEnhancedRequest.builder(Product.class)
                .conditionExpression(conditionExpression)
                .item(product)
                .ignoreNulls(true)
                .build();

        return productTable.updateItem(request);
    }

    //Delete
    public CompletableFuture<Product> deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        DeleteItemEnhancedRequest request = DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        return productTable.deleteItem(request);
    }

    public CompletableFuture<Product> deleteById(String id, Expression conditionExpression) {
        Key key = Key.builder().partitionValue(id).build();
        DeleteItemEnhancedRequest request = DeleteItemEnhancedRequest.builder()
                .key(key)
                .conditionExpression(conditionExpression)
                .build();

        return productTable.deleteItem(request);
    }
}
