package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Product;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Repository
public class ProductRepository extends DynamoDbRepositoryImpl<Product> {

    public ProductRepository(DynamoDbEnhancedAsyncClient enhancedAsyncClient) {
        super(enhancedAsyncClient);
    }

    public long countByProductCategory(String productCategory){

    }
}
