package com.example.dynamoDemo;

import com.example.dynamoDemo.configuration.DynamoDbTestConfiguration;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.ProductRepository;
import com.github.dockerjava.api.command.CreateContainerCmd;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(DynamoDbTestConfiguration.class)
public class DynamoTestContainerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DynamoDbAsyncClient asyncClient;

    @Container
    static GenericContainer dynamoDb = new GenericContainer(
            DockerImageName.parse("amazon/dynamodb-local"))
//            .withAccessToHost(true)
            .withExposedPorts(8000)

            .withFileSystemBind("./src/test/resources/dynamodb", "/home/dynamodblocal/data", BindMode.READ_WRITE)
            .withCommand("-jar DynamoDBLocal.jar -sharedDb -optimizeDbBeforeStartup -dbPath /home/dynamodblocal/data")
            .withCreateContainerCmdModifier(
                    (Consumer<CreateContainerCmd>) createContainerCmd -> {
                        createContainerCmd.withName("dynamodb-test");
//                        createContainerCmd.withHostName("localhost");
                    })
            .waitingFor(Wait.forHttp("/").forStatusCode(200))
            .waitingFor(Wait.forListeningPort());

    @Test
    public void testStuff() throws ExecutionException, InterruptedException {
        assertThat(productRepository.countDbItems()).isGreaterThanOrEqualTo(10);
        assertThat(asyncClient.listTables().get().tableNames().contains("ProductCatalog")).isTrue();
        assertThat(asyncClient.listTables().get().tableNames().contains("Customers")).isTrue();
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

