package com.example.dynamoDemo.service;


import com.example.dynamoDemo.domain.dtos.TableDescr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableDescriptionService {

    private final DynamoDbClient dynamoDbClient;

    public List<TableDescr> getTableInfo() {

        List<TableDescr> desc = new ArrayList<>();
        desc.add(describeDynamoDBTable("ProductCatalog"));
        desc.add(describeDynamoDBTable("Customers"));

        return desc;
    }

    private TableDescr describeDynamoDBTable(String tableName) {

        DescribeTableRequest request = DescribeTableRequest.builder()
                .tableName(tableName)
                .build();
        TableDescr tableDescr = new TableDescr();

        TableDescription tableInfo = dynamoDbClient.describeTable(request).table();
        if (tableInfo != null) {
            System.out.format("Table name  : %s\n", tableInfo.tableName());
            System.out.format("Table ARN   : %s\n", tableInfo.tableArn());
            System.out.format("Status      : %s\n", tableInfo.tableStatus());
            System.out.format("Item count  : %d\n", tableInfo.itemCount());
            System.out.format("Size (bytes): %d\n", tableInfo.tableSizeBytes());

            ProvisionedThroughputDescription throughputInfo = tableInfo.provisionedThroughput();
            System.out.println("Throughput");
            System.out.format("  Read Capacity : %d\n", throughputInfo.readCapacityUnits());
            System.out.format("  Write Capacity: %d\n", throughputInfo.writeCapacityUnits());

            List<AttributeDefinition> attributes = tableInfo.attributeDefinitions();
            System.out.println("Attributes");

            for (AttributeDefinition a : attributes) {
                System.out.format("  %s (%s)\n", a.attributeName(), a.attributeType());
            }

            tableDescr.toBuilder()
                    .tableName(tableInfo.tableName())
                    .tableArn(tableInfo.tableArn())
                    .tableStatus(tableInfo.tableStatus().toString())
                    .itemCount(tableInfo.itemCount())
                    .tableSizeBytes(tableInfo.tableSizeBytes())
                    .readCapacity(throughputInfo.readCapacityUnits())
                    .writeCapacity(throughputInfo.writeCapacityUnits())
                    .attributes(attributes).build();

        }

        return tableDescr;
    }
}
