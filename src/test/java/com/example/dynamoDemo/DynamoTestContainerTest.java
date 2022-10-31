package com.example.dynamoDemo;

import com.example.dynamoDemo.configuration.DynamoDbTestConfiguration;
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
import software.amazon.awssdk.services.dynamodb.model.DeleteTableRequest;

import java.util.Random;
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

//    @Autowired
//    private static FixedHostPortGenericContainer dynamodb;

    @BeforeAll
    public void createTables() {
        System.out.println();
        System.out.println("Initializing... ");
        createCustomersTableAsync(asyncClient).join();
//                .whenComplete(CompletableFutureUtils.doOnError(
//                        throwable -> log.error("Table creation failed", throwable.getCause())));

        createProductCatalogTableAsync(asyncClient).join();
//                .whenComplete(CompletableFutureUtils.doOnError(
//                        throwable -> log.error("Table creation failed", throwable.getCause())));
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

//    @Test
//    public void testStuff1() throws ExecutionException, InterruptedException {
//
//        HashMap<String,AttributeValue> keyToGet = new HashMap<>();
//        keyToGet.put("Id", AttributeValue.builder()
//                .s("to6yu35g76y895h76y4")
//                .build());
//
//        GetItemRequest request = GetItemRequest.builder()
//                .tableName("Customers")
//                .key(keyToGet)
//                .consistentRead(true)
//                .build();
//
//        CompletableFuture<GetItemResponse> ss = asyncClient.getItem(request)
//                .whenComplete(CompletableFutureUtils.doOnError(throwable -> log.error("DynamoDB PUT AsynchronousRequest failed", throwable.getCause())))
//                .thenApply((GetItemResponse)i -> System.out.println(i));
//
//        System.out.println(ss.join());
//
////        System.out.println(asyncClient.getItem(request).join().item());
//    }


    @Test
    public void testStuff2() throws ExecutionException, InterruptedException {
//        System.out.println("1>>  " + asyncClient.listTables().get().tableNames().size());
        customerService.generateNewCustomers(1);
//        assertThat(asyncClient.listTables().get().tableNames().size()).isTrue();
        System.out.println("2>>  " + customerRepository.countDbItems());
        System.out.println("2>>  " + customerRepository.countDbItems());
//        System.out.println("3>>  " + asyncClient.listTables().get().tableNames().size());
//        TimeUnit.SECONDS.sleep(30);

//        TimeUnit.SECONDS.sleep(8);
//        System.out.println("4>>  " + asyncClient.listTables().get().tableNames().size());
        asyncClient.deleteTable(DeleteTableRequest.builder()
                .tableName(CUSTOMERS_TABLENAME)
                .build());
        createCustomersTableAsync(asyncClient);
    }

    @Test
    public void testStuff3() throws ExecutionException, InterruptedException {

        System.out.println(asyncClient.listTables().get().tableNames().size());
    }

    @Test
    public void showTables() throws ExecutionException, InterruptedException {

    }



    @Test
    public void testItemCount() throws ExecutionException, InterruptedException {
        assertThat(productRepository.countDbItems()).isGreaterThanOrEqualTo(10);
    }

    @Test
    public void testRecordContent() throws ExecutionException, InterruptedException {
        Product product = productRepository.findById("203").get();
        assertThat(product.getProductCategory()).isEqualTo("Bicycle");
    }

    @Test
    public void testAddRecord() throws ExecutionException, InterruptedException {

        String newId = String.valueOf(new Random().nextInt());
        Product product = Product.builder()
                .id(newId)
                .inPublication(true)
                .isbn("111-1111155511")
                .pageCount(123)
                .price(100)
                .productCategory("Book")
                .title("Default test book")
                .build();

        productRepository.save(product);
        Product testProduct = productRepository.findById(newId).get();
        assertThat(testProduct.getTitle()).isEqualTo("Default test book");
    }
}

