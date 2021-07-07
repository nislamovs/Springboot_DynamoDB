package com.example.dynamoDemo.controllers;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        log.info("Getting all products from the db");
        return ok(service.getProducts());
    }

    @GetMapping("/products/{productcat}")
    public ResponseEntity<?> getProductsByProductCategory(@PathVariable("productcat") final String productCategory) {
        log.info("Getting products by product category = {} from the db", productCategory);
        return ok(service.getProductsByProductCategory(productCategory));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") final String id)
        throws ProductNotFoundException {
        log.info("Getting product by id = {} from the db", id);
        return ok(service.getProductById(id));
    }

    @GetMapping("/products/count/{productcat}")
    public ResponseEntity<?> getCountByProductCategory(@PathVariable("productcat") final String productcat) {

        return ok(service.getProductsCountByProductCategory(productcat));
    }

    @GetMapping("/products/count")
    public ResponseEntity<?> getProductCount() {

        return ok(service.getProductsCount());
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") final String id) throws ProductNotFoundException {
        log.info("Delete product by id = {} from the db", id);
        service.delete(id);

        return noContent().build();
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<?> update(@PathVariable("id") final String id,
                                    @RequestBody final ProductDto dto) throws ProductNotFoundException {
        log.info("Updating product by id = {} into the db", id);
        service.update(id, dto);

        return noContent().build();
    }

    @PostMapping("/product")
    public ResponseEntity<?> save(@RequestBody final ProductDto dto) {
        log.info("Saving new product = {} into the db", dto.toString());
        service.save(dto);

        return ok().build();
    }

}
