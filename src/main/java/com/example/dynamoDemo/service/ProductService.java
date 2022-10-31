package com.example.dynamoDemo.service;

import static com.example.dynamoDemo.domain.exceptions.ProductNotFoundException.withMessage;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.dtos.ProductDtos;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.mappers.ProductMapper;
import com.example.dynamoDemo.models.Product;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.example.dynamoDemo.repository.ProductRepository;
import com.example.dynamoDemo.utils.CompletableFutureUtils;
import com.example.dynamoDemo.utils.DbUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Mono<ProductDtos>  getProducts() {
        return Flux.from(productRepository.findAll())
                .next()
                .map(page -> {
                    List<ProductDto> products = page.items().stream().map(productMapper::toDTO).collect(Collectors.toList());
                    return ProductDtos.builder()
                            .items(products)
                            .lastEvaluatedKey(new DbUtils<Product>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public Mono<ProductDtos> getProducts(long lastItemId, long size) {

        return Flux.from(productRepository.findAll(String.valueOf(lastItemId * size), parseInt(String.valueOf(size))))
                .next()
                .map(page -> {
                    List<ProductDto> products = page.items().stream().map(productMapper::toDTO).collect(Collectors.toList());
                    return ProductDtos.builder()
                            .items(products)
                            .lastEvaluatedKey(new DbUtils<Product>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public Mono<Long> getProductsCount() throws ExecutionException, InterruptedException {
        return Mono.just(productRepository.countDbItems());
    }

    public Mono<ProductDtos> getProductsByProductCategory(final String productCategory) {
        return Flux.from(productRepository.findByProductCategory(productCategory))
                .next()
                .map(page -> {
                    List<ProductDto> products = page.items().stream().map(productMapper::toDTO).collect(Collectors.toList());
                    return ProductDtos.builder()
                            .items(products)
                            .lastEvaluatedKey(new DbUtils<Product>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public Mono<ProductDto> getProductById(final String id) throws ExecutionException, InterruptedException {
        CompletableFuture<ProductDto> product = productRepository.findById(id)
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error(format("Product by id [%s] was not found.", id), throwable.getCause())))
                .thenApply(productMapper::toDTO);

        return Mono.fromCompletionStage(product)
                .switchIfEmpty(error(() -> withMessage(format("Product by id [%s] was not found.", id))));
    }

    public void update(final String id, final ProductDto productDto) throws ProductNotFoundException, ExecutionException, InterruptedException {
        getProductById(id);
        productRepository.update(productMapper.toDAO(productDto))
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB update failed", throwable.getCause())));
    }

    public void delete(final String id) throws ProductNotFoundException, ExecutionException, InterruptedException {
        getProductById(id);
        productRepository.deleteById(id)
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB delete failed", throwable.getCause())));
    }

    public void save(final ProductDto productDto) {
        productRepository.save(productMapper.toDAO(productDto))
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB save failed", throwable.getCause())));
    }
}
