package com.example.dynamoDemo.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbFlatten;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {

    @Getter(onMethod_ = {@DynamoDbPartitionKey, @DynamoDbAttribute(value = "Id")})
    private String id;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Firstname")})
    private String firstname;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Lastname")})
    private String lastname;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "RegisteredDate")})
    private String registeredDate;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Email")})
    private String email;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Address"), @DynamoDbFlatten})
    private Address address;
}
