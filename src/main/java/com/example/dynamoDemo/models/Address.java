package com.example.dynamoDemo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import static com.example.dynamoDemo.configuration.DynamoDbConfiguration.COUNTRY_INDEX;

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

    @Getter(onMethod_ = {@DynamoDbSecondaryPartitionKey(indexNames = COUNTRY_INDEX), @DynamoDbAttribute(value = "Country")})
    private String country;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "PhoneNumber")})
    private String phoneNumber;
}
