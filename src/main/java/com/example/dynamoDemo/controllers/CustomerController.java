package com.example.dynamoDemo.controllers;

import com.example.dynamoDemo.controllers.apidocs.iCustomerController;
import com.example.dynamoDemo.domain.dtos.CustomerDto;
import com.example.dynamoDemo.domain.exceptions.CustomerNotFoundException;
import com.example.dynamoDemo.domain.exceptions.ProductNotFoundException;
import com.example.dynamoDemo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController implements iCustomerController {

    private final CustomerService customerService;

    @Override
    @GetMapping("/customer")
    public Flux<?> getCustomers(@RequestParam(value = "page", defaultValue = "0") long page,
                               @RequestParam(value = "size", defaultValue = "10") long size) {
        log.info("Getting all customers from the db");
        return Flux.just(customerService.getCustomers(page, size));
    }

    @Override
    @GetMapping("/customer/{email}")
    public Flux<?> getCustomerByEmail(@PathVariable("email") final String email) {
        log.info("Getting customer by email = {} from the db", email);
        return Flux.just(customerService.getCustomerByEmail(email));
    }

    @Override
    @GetMapping("/customer/{id}")
    public Mono<?> getCustomerById(@PathVariable("id") final String id) throws CustomerNotFoundException, ExecutionException, InterruptedException {
        log.info("Getting customer by id = {} from the db", id);
        return Mono.just(customerService.getCustomerById(id));
    }

    @Override
    @GetMapping("/customer/count")
    public Mono<?> getCustomerCount() {
        return Mono.just(customerService.getCustomersCount());
    }

    @Override
    @DeleteMapping("/customer/{id}")
    public Mono<?> deleteCustomer(@PathVariable("id") final String id) throws CustomerNotFoundException, ExecutionException, InterruptedException {
        log.info("Delete customer by id = {} from the db", id);
        customerService.delete(id);

        return Mono.just(noContent().build());
    }

    @Override
    @PutMapping("/customer/{id}")
    public Mono<?> updateCustomer(@PathVariable("id") final String id,
                                  @RequestBody final CustomerDto dto) throws CustomerNotFoundException, ExecutionException, InterruptedException {
        log.info("Updating customer by id = {} into the db", id);
        customerService.update(id, dto);

        return Mono.just(noContent().build());
    }

    @Override
    @PostMapping("/customer")
    public Mono<?> saveCustomer(@RequestBody final CustomerDto dto) {
        log.info("Saving new customer = {} into the db", dto.toString());
        customerService.save(dto);

        return Mono.just(ok().build());
    }

    @Override
    @PostMapping("/customer/generate/{count}")
    public Mono<?> generateCustomers(@PathVariable("count") final Integer count) {
        log.info("Generating {} new customers...", count.toString());
        customerService.generateNewCustomers(count);

        return Mono.just(ok().build());
    }
}
