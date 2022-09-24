package com.example.dynamoDemo.service;

import static java.lang.String.format;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.mappers.ProductMapper;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.DynamoDbRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.example.dynamoDemo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper productMapper;

    public List<ProductDto> getProducts() {

//        repository.put()

        final Iterable<Product> allProducts = repository.ge();
        return StreamSupport.stream(allProducts.spliterator(), false)
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<ProductDto> getProducts(long page, long size) {
        final Page<Product> allProducts = repository.findAll(
//            PageRequest.of((int)page, (int)size, Sort.by("id").ascending()));
            PageRequest.of((int)page, (int)size, Sort.unsorted()));

        return StreamSupport.stream(allProducts.spliterator(), false)
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }

    public ProductDto getProductById(final String id) throws ProductNotFoundException {
        var product = repository.findById(id);
        if (product.isPresent())
            return productMapper.toDTO(product.get());
        else
            throw new ProductNotFoundException(format("Product by id [%s] was not found.", id));
    }

    public void save(final ProductDto productDto) {
        repository.save(productMapper.toDAO(productDto));
    }

    public void update(final String id, final ProductDto productDto) throws ProductNotFoundException {
        getProductById(id);
        repository.save(productMapper.toDAO(productDto));
    }

    public void delete(final String id) throws ProductNotFoundException {
        getProductById(id);
        repository.deleteById(id);
    }

    public Long getProductsCount() {
        return repository.count();
    }

    public Long getProductsCountByProductCategory(final String productCategory) {
        return repository.countByProductCategory(productCategory);
    }

    public List<ProductDto> getProductsByProductCategory(final String productCategory) {
        return repository.findAllByProductCategory(productCategory)
            .stream().map(productMapper::toDTO).collect(Collectors.toList());
    }
}
