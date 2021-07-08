package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Product;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
@EnableScanCount
public interface ProductRepository extends DynamoDBPagingAndSortingRepository<Product, String> {

    long countByProductCategory(String productCategory);

    List<Product> findAllByProductCategory(String productCategory);

    Page<Product> findAll(Pageable pageable);
}
