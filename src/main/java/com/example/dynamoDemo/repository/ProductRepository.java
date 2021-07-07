package com.example.dynamoDemo.repository;

import com.example.dynamoDemo.models.Product;
import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@EnableScan
@Repository
public interface ProductRepository extends CrudRepository<Product, String> {

    @EnableScanCount
    long countByProductCategory(String productCategory);

    List<Product> findAllByProductCategory(String productCategory);
}
