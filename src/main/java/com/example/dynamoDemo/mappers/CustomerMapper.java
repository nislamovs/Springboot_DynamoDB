package com.example.dynamoDemo.mappers;

import com.example.dynamoDemo.domain.dtos.CustomerDto;
import com.example.dynamoDemo.models.Customer;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Component
public interface CustomerMapper {

  @Mapping(source = "id", target = "id")
  @ConvertAddress
  CustomerDto toDTO(Customer customer);

  @Mapping(source = "id", target = "id")
  @ConvertAddress
  Customer toDAO(CustomerDto customerDto);
}
