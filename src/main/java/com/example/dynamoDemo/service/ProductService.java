package com.example.dynamoDemo.service;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;


    public List<Product> getProducts() {
        final Iterable<Product> allProducts = repository.findAll();
        return StreamSupport.stream(allProducts.spliterator(), false).collect(Collectors.toList());
    }

    public Product getProductById(final String id) throws ProductNotFoundException {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(""));
    }

    public void save(final ProductDto dto) {
        final Product b = createProductBuilder(dto);
        repository.save(b);
    }

    public void update(final String id, final ProductDto dto) throws ProductNotFoundException {
        getProductById(id);
        final Product ProductToBeUpdated = createProductBuilder(dto).toBuilder().id(id).build();
        repository.save(ProductToBeUpdated);
    }

    public void delete(final String id) throws ProductNotFoundException {
        getProductById(id);
        repository.deleteById(id);
    }

    public long getProductsCount() {
        return repository.count();
    }

    public long getProductsCountByProductCategory(final String productCategory) {
        return repository.countByProductCategory(productCategory);
    }

    public List<Product> getProductsByProductCategory(final String productCategory) {
        return repository.findAllByProductCategory(productCategory);
    }

    private Product createProductBuilder(final ProductDto dto) {
        return Product.builder()
            .build();
    }
}
