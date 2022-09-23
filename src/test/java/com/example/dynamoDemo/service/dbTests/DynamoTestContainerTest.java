package com.example.dynamoDemo.service.dbTests;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.example.dynamoDemo.models.Product;
import com.example.dynamoDemo.repository.ProductRepository;
import com.github.dockerjava.api.command.CreateContainerCmd;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Random;
import java.util.function.Consumer;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DynamoTestContainerTest {

    @Autowired
    private ProductRepository productRepository;
    private static AmazonDynamoDB client;

    private static String accessKey = "DUMMYIDEXAMPLE";
    private static String secretKey = "DUMMYEXAMPLEKEY";
    private static String region = "us-west-2";

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

    @BeforeAll
    public static void init() {
        var endpointUrl = String.format("http://localhost:%d", dynamoDb.getFirstMappedPort());
        AWSStaticCredentialsProvider awsCredProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(endpointUrl, region);

        client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(awsCredProvider)
                .build();
    }

    @Test
    public void testStuff() {
        assertThat(productRepository.count()).isGreaterThanOrEqualTo(10);
        assertThat(client.listTables().getTableNames().contains("ProductCatalog")).isTrue();
    }

    @Test
    public void testRecordContent() {
        Product product = productRepository.findById("203").get();
        assertThat(product.getProductCategory()).isEqualTo("Bicycle");
    }

    @Test
    public void testAddRecord() {

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

