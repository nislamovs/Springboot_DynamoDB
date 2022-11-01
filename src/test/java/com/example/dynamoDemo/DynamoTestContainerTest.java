package com.example.dynamoDemo;

import com.example.dynamoDemo.configuration.DynamoDbTestConfiguration;
import com.example.dynamoDemo.models.Customer;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.CustomerRepository;
import com.example.dynamoDemo.repository.ProductRepository;
import com.example.dynamoDemo.service.CustomerService;
import com.example.dynamoDemo.utils.CompletableFutureUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.dynamoDemo.utils.TestFactory.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
@Import(DynamoDbTestConfiguration.class)
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DynamoTestContainerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DynamoDbAsyncClient asyncClient;

    @BeforeAll
    public void createTables() {
        System.out.println();
        System.out.println("Initializing... ");
        createCustomersTableAsync(asyncClient).join();
        createProductCatalogTableAsync(asyncClient).join();
        System.out.print("Done!!!");
        System.out.println();
    }

    @AfterAll
    public void cleanAll(){
        System.out.println();
        System.out.println("Cleaning up... ");
        asyncClient.deleteTable(DeleteTableRequest.builder().tableName(CUSTOMERS_TABLENAME).build())
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("Table delete failed", throwable.getCause())));

        asyncClient.deleteTable(DeleteTableRequest.builder().tableName(PRODUCT_CATALOG_TABLENAME).build())
                .whenComplete(CompletableFutureUtils.doOnError(
                        throwable -> log.error("Table delete failed", throwable.getCause())));
        System.out.print("Done!!!");
        System.out.println();
    }

    @Test
    public void testTables() throws ExecutionException, InterruptedException {
        assertThat(asyncClient.listTables().get().tableNames().contains(PRODUCT_CATALOG_TABLENAME)).isTrue();
        assertThat(asyncClient.listTables().get().tableNames().contains(CUSTOMERS_TABLENAME)).isTrue();
        System.out.println(asyncClient.listTables().get().tableNames());
    }

    @Test
    public void testRecordCount() throws ExecutionException, InterruptedException {
        long count = customerRepository.countDbItems();
        customerService.generateNewCustomers(1);
        assertThat( customerRepository.countDbItems()).isEqualTo(count+1);
        System.out.println(customerRepository.countDbItems());
    }

    @Test
    public void testGetCustomer() {

        List<Customer> customers = customerService.generateNewCustomers(1);

        HashMap<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("Id", AttributeValue.builder()
                .s(customers.get(0).getId())
                .build());
        keyToGet.put("Email", AttributeValue.builder()
                .s(customers.get(0).getEmail())
                .build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName("Customers")
                .key(keyToGet)
                .consistentRead(true)
                .build();

        GetItemResponse response = asyncClient.getItem(request).join();
        System.out.println(response.toString());
        assertThat(response.item().get("Firstname").s()).isEqualTo(customers.get(0).getFirstname());
    }

//    @Test
//    public void testRecordContent() throws ExecutionException, InterruptedException {
//        Product product = productRepository.findById("203").get();
//        assertThat(product.getProductCategory()).isEqualTo("Bicycle");
//    }
//
//    @Test
//    public void testAddRecord() throws ExecutionException, InterruptedException {
//
//        String newId = String.valueOf(new Random().nextInt());
//        Product product = Product.builder()
//                .id(newId)
//                .inPublication(true)
//                .isbn("111-1111155511")
//                .pageCount(123)
//                .price(100)
//                .productCategory("Book")
//                .title("Default test book")
//                .build();
//
//        productRepository.save(product).join();
//        Product testProduct = productRepository.findById(newId).join();
//        assertThat(testProduct.getTitle()).isEqualTo("Default test book");
//    }
}

