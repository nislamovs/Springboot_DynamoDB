package com.example.dynamoDemo.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {

    private String id;
    private String firstname;
    private String lastname;
    private String registeredDate;
    private String email;
    private Address address;

    @Data
    @Builder
    public static class Address {
        private String street;
        private String postcode;
        private String city;
        private String country;
        private String phoneNumber;
    }
}

