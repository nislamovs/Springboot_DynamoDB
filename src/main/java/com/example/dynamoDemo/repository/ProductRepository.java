package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends DynamoDbRepository<Product>{

        long countByProductCategory(String productCategory);
//
//    List<Product> findAllByProductCategory(String productCategory);
//
//    Page<Product> findAll(Pageable pageable);
}
