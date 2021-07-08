package com.example.dynamoDemo.controllers;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

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

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProductController implements iProductController {

    private final ProductService service;

    @Override
    @GetMapping("/products")
    public Flux<?> getProducts(@RequestParam(value = "page", defaultValue = "0") long page,
                               @RequestParam(value = "size", defaultValue = "10") long size) {
        log.info("Getting all products from the db");
        return Flux.just(service.getProducts(page, size));
    }

    @Override
    @GetMapping("/products/{productcat}")
    public Flux<?> getProductsByProductCategory(@PathVariable("productcat") final String productCategory) {
        log.info("Getting products by product category = {} from the db", productCategory);
        return Flux.just(service.getProductsByProductCategory(productCategory));
    }

    @Override
    @GetMapping("/product/{id}")
    public Mono<?> getProductById(@PathVariable("id") final String id)
        throws ProductNotFoundException {
        log.info("Getting product by id = {} from the db", id);
        return Mono.just(service.getProductById(id));
    }

    @Override
    @GetMapping("/products/count/{productcat}")
    public Mono<?> getCountByProductCategory(@PathVariable("productcat") final String productcat) {

        return Mono.just(service.getProductsCountByProductCategory(productcat));
    }

    @Override
    @GetMapping("/products/count")
    public Mono<?> getProductCount() {

        return Mono.just(service.getProductsCount());
    }

    @Override
    @DeleteMapping("/product/{id}")
    public Mono<?> deleteProduct(@PathVariable("id") final String id) throws ProductNotFoundException {
        log.info("Delete product by id = {} from the db", id);
        service.delete(id);

        return Mono.just(noContent().build());
    }

    @Override
    @PutMapping("/product/{id}")
    public Mono<?> update(@PathVariable("id") final String id,
                                    @RequestBody final ProductDto dto) throws ProductNotFoundException {
        log.info("Updating product by id = {} into the db", id);
        service.update(id, dto);

        return Mono.just(noContent().build());
    }

    @Override
    @PostMapping("/product")
    public Mono<?> save(@RequestBody final ProductDto dto) {
        log.info("Saving new product = {} into the db", dto.toString());
        service.save(dto);

        return Mono.just(ok().build());
    }

}
