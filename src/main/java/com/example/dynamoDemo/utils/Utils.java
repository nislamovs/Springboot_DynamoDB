package com.example.dynamoDemo.utils;

import com.example.dynamoDemo.domain.dtos.CustomerDto;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class Utils {

    private static final Faker faker;

    static {
        faker = new Faker();
    }

    public static CustomerDto generateCustomer() {
        return CustomerDto.builder()
                .id(UUID.randomUUID().toString().replaceAll("-", ""))
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .registeredDate(Instant.now().toString())
                .email(faker.internet().emailAddress())
                .address(
                        CustomerDto.Address.builder()
                                .city(faker.address().city())
                                .street(faker.address().streetAddress())
                                .postcode(faker.address().zipCode())
                                .country(faker.address().state())
                                .phoneNumber(faker.phoneNumber().phoneNumber())
                                .build()
                ).build();
    }
}
