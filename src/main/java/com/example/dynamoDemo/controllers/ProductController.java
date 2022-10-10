package com.example.dynamoDemo.controllers;

import com.example.dynamoDemo.controllers.apidocs.iProductController;
import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController implements iProductController {

    private final ProductService productService;

    @Override
    @GetMapping("/product")
    public Flux<?> getProducts(@RequestParam(value = "page", defaultValue = "0") long page,
                               @RequestParam(value = "size", defaultValue = "10") long size) {
        log.info("Getting all products from the db");
        return Flux.just(productService.getProducts(page, size));
    }

    @Override
    @GetMapping("/product/{productcat}")
    public Flux<?> getProductsByProductCategory(@PathVariable("productcat") final String productCategory) {
        log.info("Getting products by product category = {} from the db", productCategory);
        return Flux.just(productService.getProductsByProductCategory(productCategory));
    }

    @Override
    @GetMapping("/product/{id}")
    public Mono<?> getProductById(@PathVariable("id") final String id)
            throws ProductNotFoundException, ExecutionException, InterruptedException {
        log.info("Getting product by id = {} from the db", id);
        return Mono.just(productService.getProductById(id));
    }

    @Override
    @GetMapping("/product/count")
    public Mono<?> getProductCount() {
        return Mono.just(productService.getProductsCount());
    }

    @Override
    @DeleteMapping("/product/{id}")
    public Mono<?> deleteProduct(@PathVariable("id") final String id) throws ProductNotFoundException, ExecutionException, InterruptedException {
        log.info("Delete product by id = {} from the db", id);
        productService.delete(id);

        return Mono.just(noContent());
    }

    @Override
    @PutMapping("/product/{id}")
    public Mono<?> update(@PathVariable("id") final String id,
                          @RequestBody final ProductDto dto) throws ProductNotFoundException, ExecutionException, InterruptedException {
        log.info("Updating product by id = {} into the db", id);
        productService.update(id, dto);

        return Mono.just(accepted());
    }

    @Override
    @PostMapping("/product")
    public Mono<?> save(@RequestBody final ProductDto dto) {
        log.info("Saving new product = {} into the db", dto.toString());
        productService.save(dto);

        return Mono.just(ok());
    }

}
