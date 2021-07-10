package com.example.dynamoDemo.mappers;

import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.models.Product;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface ProductMapper {

  @Mapping(source = "id", target = "id")
  ProductDto toDTO(Product product);

  @Mapping(source = "id", target = "id")
  Product toDAO(ProductDto productDto);
}
