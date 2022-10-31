package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.TableDescription;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static io.netty.util.internal.StringUtil.isNullOrEmpty;

@Repository
@RequiredArgsConstructor
public class CustomerRepository {

    private final DynamoDbEnhancedAsyncClient enhancedAsyncClient;
    private DynamoDbAsyncTable<Customer> customerTable;
    private final DynamoDbClient dynamoDbClient;

    @PostConstruct
    void init() {
        customerTable = enhancedAsyncClient.table("Customers", TableSchema.fromBean(Customer.class));
    }

    //Pagination
    public SdkPublisher<Page<Customer>> findAll(String lastId, Integer limit) {

        Map<String, AttributeValue> exclusiveStartKey = !isNullOrEmpty(lastId)
                ? Map.of("Id", AttributeValue.builder().s(lastId).build())
                : null;

        var itemsPerPage = ( limit == null || limit <= 0 || limit > 20 ) ? 20 : limit;

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .exclusiveStartKey(exclusiveStartKey)
                .limit(itemsPerPage)
                .build();

        return customerTable.scan(request).limit(1);
    }

    public PagePublisher<Customer> findAll() {
        return customerTable.scan();
    }

    //Find item
    public CompletableFuture<Customer> findById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        GetItemEnhancedRequest request = GetItemEnhancedRequest.builder()
                .key(key)
                .consistentRead(true)
                .build();

        return customerTable.getItem(request);
    }

    public PagePublisher<Customer> findCustomerByEmail(String email) {

        AttributeValue att = AttributeValue.builder()
                .s(email)
                .build();

        Map<String, AttributeValue> expressionValues = new HashMap<>();
        expressionValues.put(":email", att);

        Expression expression = Expression.builder()
                .expression("Email = :email")
                .expressionValues(expressionValues)
                .build();

        ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                .consistentRead(true)
                .filterExpression(expression)
                .build();

        return customerTable.scan(request);
    }

//Count

    public long countDbItems(){
        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(customerTable.tableName())
                .build();

        TableDescription tableInfo = dynamoDbClient.describeTable(request).table();
        return tableInfo.itemCount();
    }

//Save
    public CompletableFuture<Void> save(Customer product) {
        PutItemEnhancedRequest<Customer> request = PutItemEnhancedRequest.builder(Customer.class)
                .item(product)
                .build();

        return customerTable.putItem(request);
    }

    public CompletableFuture<Void> save(Customer product, Expression conditionExpression) {
        PutItemEnhancedRequest<Customer> request = PutItemEnhancedRequest.builder(Customer.class)
                .conditionExpression(conditionExpression)
                .item(product)
                .build();

        return customerTable.putItem(request);
    }

//Update
    public CompletableFuture<Customer> update(Customer product) {
        UpdateItemEnhancedRequest<Customer> request = UpdateItemEnhancedRequest.builder(Customer.class)
                .item(product)
                .ignoreNulls(true)
                .build();

        return customerTable.updateItem(request);
    }

    public CompletableFuture<Customer> update(Customer product, Expression conditionExpression) {
        UpdateItemEnhancedRequest<Customer> request = UpdateItemEnhancedRequest.builder(Customer.class)
                .conditionExpression(conditionExpression)
                .item(product)
                .ignoreNulls(true)
                .build();

        return customerTable.updateItem(request);
    }

//Delete
    public CompletableFuture<Customer> deleteById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        DeleteItemEnhancedRequest request = DeleteItemEnhancedRequest.builder()
                .key(key)
                .build();

        return customerTable.deleteItem(request);
    }

    public CompletableFuture<Customer> deleteById(String id, Expression conditionExpression) {
        Key key = Key.builder().partitionValue(id).build();
        DeleteItemEnhancedRequest request = DeleteItemEnhancedRequest.builder()
            .key(key)
            .conditionExpression(conditionExpression)
            .build();

        return customerTable.deleteItem(request);
    }

    public CompletableFuture<BatchWriteResult> batchWrite(List<Customer> customerList) {
        WriteBatch.Builder<Customer> wb = WriteBatch.builder(Customer.class)          // add items to the Customer table
                .mappedTableResource(customerTable);

        customerList.forEach(item -> wb.addPutItem(builder -> builder.item(item)));
        wb.build();

        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
                .writeBatches((Collection<WriteBatch>) wb)
                .build();

        return enhancedAsyncClient.batchWriteItem(batchWriteItemEnhancedRequest);
    }
}
