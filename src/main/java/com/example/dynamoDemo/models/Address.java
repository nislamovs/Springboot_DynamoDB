package com.example.dynamoDemo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Street")})
    private String street;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Postcode")})
    private String postcode;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "City")})
    private String city;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Country")})
    private String country;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "PhoneNumber")})
    private String phoneNumber;
}
