package com.example.dynamoDemo.service;


import com.example.dynamoDemo.domain.dtos.CustomerDto;
import com.example.dynamoDemo.domain.dtos.CustomerDtos;
import com.example.dynamoDemo.domain.dtos.ProductDto;
import com.example.dynamoDemo.domain.dtos.ProductDtos;
import com.example.dynamoDemo.domain.exceptions.CustomerNotFoundException;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.mappers.CustomerMapper;
import com.example.dynamoDemo.models.Customer;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.CustomerRepository;
import com.example.dynamoDemo.utils.CompletableFutureUtils;
import com.example.dynamoDemo.utils.DbUtils;
import com.example.dynamoDemo.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.example.dynamoDemo.domain.exceptions.ProductNotFoundException.withMessage;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public Mono<CustomerDtos> getCustomers() {
        return Flux.from(customerRepository.findAll())
                .next()
                .map(page -> {
                    List<CustomerDto> customers = page.items().stream().map(customerMapper::toDTO).collect(Collectors.toList());
                    return CustomerDtos.builder()
                            .items(customers)
                            .lastEvaluatedKey(new DbUtils<Customer>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public Mono<CustomerDtos> getCustomers(long lastItemId, long size) {
        return Flux.from(customerRepository.findAll(String.valueOf(lastItemId * size), parseInt(String.valueOf(size))))
                .next()
                .map(page -> {
                    List<CustomerDto> customers = page.items().stream().map(customerMapper::toDTO).collect(Collectors.toList());
                    return CustomerDtos.builder()
                            .items(customers)
                            .lastEvaluatedKey(new DbUtils<Customer>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public Mono<CustomerDto> getCustomerById(final String id)  {
        CompletableFuture<CustomerDto> customer = customerRepository.findById(id)
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error(format("Customer by id [%s] was not found.", id), throwable.getCause())))
                .thenApply(customerMapper::toDTO);

        return Mono.fromCompletionStage(customer)
                .switchIfEmpty(error(() -> withMessage(format("Customer by id [%s] was not found.", id))));
    }

    public Mono<Long> getCustomersCount() {
        return Mono.just(customerRepository.countDbItems());
    }

    public Mono<CustomerDtos> getCustomerByEmail(final String email) {
        return Flux.from(customerRepository.findCustomerByEmail(email))
                .next()
                .map(page -> {
                    List<CustomerDto> customers = page.items().stream().map(customerMapper::toDTO).collect(Collectors.toList());
                    return CustomerDtos.builder()
                            .items(customers)
                            .lastEvaluatedKey(new DbUtils<Customer>().getLastEvaluatedKey(page))
                            .build();
                });
    }

    public void update(final String id, final CustomerDto customerDto) throws ExecutionException, InterruptedException {
        getCustomerById(id);
        customerRepository.update(customerMapper.toDAO(customerDto))
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB update failed", throwable.getCause())));
    }

    public void delete(final String id) throws ExecutionException, InterruptedException {
        getCustomerById(id);
        customerRepository.deleteById(id)
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB delete failed", throwable.getCause())));
    }

    public void save(final CustomerDto customerDto) {
        customerRepository.save(customerMapper.toDAO(customerDto))
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB save failed", throwable.getCause())));
    }

    public void generateNewCustomers(Integer count) {
        List<CustomerDto> customers = new ArrayList<>();
        for (int n = 0; n < count; n++) {
            customers.add(Utils.generateCustomer());
        }
        CustomerDtos customerList = CustomerDtos.builder().items(customers).build();
        List<Customer> customersDaos = customerList.getItems().stream()
                .map(customerMapper::toDAO).collect(Collectors.toList());

        customerRepository.batchWrite(customersDaos)
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("DynamoDB batch save failed", throwable.getCause())));
    }
}
