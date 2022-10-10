package com.example.dynamoDemo.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    @Getter(onMethod_ = {@DynamoDbPartitionKey, @DynamoDbAttribute(value = "Id")})
    private String id;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "InPublication")})
    private Boolean inPublication;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "ISBN")})
    private String isbn;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "PageCount")})
    private Integer pageCount;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Price")})
    private Integer price;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Authors")})
    private List<String> authors;

    @Getter(onMethod_ = {@DynamoDbSortKey, @DynamoDbAttribute(value = "ProductCategory")})
    private String productCategory;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Title")})
    private String title;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Dimensions")})
    private String dimensions;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Brand")})
    private String brand;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Description")})
    private String description;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "Color")})
    private List<String> color;

    @Getter(onMethod_ = {@DynamoDbAttribute(value = "BicycleType")})
    private String bicycleType;
}
