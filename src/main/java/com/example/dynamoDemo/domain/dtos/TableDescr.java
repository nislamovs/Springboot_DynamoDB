package com.example.dynamoDemo.domain.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TableDescr {

    private String tableName;
    private String tableArn;
    private String tableStatus;
    private Long itemCount;
    private Long tableSizeBytes;

    private Long readCapacity;
    private Long writeCapacity;
    private List<AttributeDefinition> attributes;

}

