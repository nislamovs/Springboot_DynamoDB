package com.example.dynamoDemo.configuration;


import com.github.dockerjava.api.command.CreateContainerCmd;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClientBuilder;
import software.amazon.awssdk.utils.ThreadFactoryBuilder;

import java.net.URI;
import java.nio.file.Paths;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR;

@TestConfiguration
@Testcontainers
public class DynamoDbTestConfiguration {

    @Value("${amazon.dynamodb.endpoint}")
    private String endpoint;
    @Value("${amazon.aws.accesskey}")
    private String accesskey;
    @Value("${amazon.aws.secretkey}")
    private String secretkey;
    @Value("${amazon.aws.region}")
    private String region;

    @Container
    FixedHostPortGenericContainer dynamoDb;

    @Bean
    @Primary
    public FixedHostPortGenericContainer dynamoDbContainer() {
        dynamoDb = new FixedHostPortGenericContainer("amazon/dynamodb-local:latest")
                .withFixedExposedPort(8000, 8000);
        dynamoDb.addFileSystemBind(Paths.get("src/test/resources/dynamodb").toAbsolutePath().toString(), "/home/dynamodblocal/data", BindMode.READ_WRITE);
        dynamoDb.waitingFor(Wait.forHttp("/").forStatusCode(200));
        dynamoDb.waitingFor(Wait.forListeningPort());
        dynamoDb.withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("dev.exceptionly")));
        dynamoDb.withExposedPorts(8000);
        dynamoDb.withCommand("-jar DynamoDBLocal.jar -sharedDb -optimizeDbBeforeStartup -dbPath /home/dynamodblocal/data");
        dynamoDb.withCreateContainerCmdModifier(
                (Consumer<CreateContainerCmd>) createContainerCmd -> {
                    createContainerCmd.withName("dynamodb-test");
                });
        dynamoDb.start();

        System.out.println(">>>>>>>>>>>          "+dynamoDb.getFirstMappedPort());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getPortBindings());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getExposedPorts());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getBinds());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getBoundPortNumbers());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getContainerInfo().toString());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getContainerInfo().getNetworkSettings().getIpAddress());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getHost());
        System.out.println(">>>>>>>>>>>          "+dynamoDb.getContainerInfo().getHostnamePath());
        System.out.println("");
        System.out.println("");

        return dynamoDb;
    }

    @Bean
    public ThreadPoolExecutor dynamoDBExecutor() {
        int cores = Runtime.getRuntime().availableProcessors();
        int corePoolSize = Math.max(8, cores);
        int maxPoolSize = Math.max(100, cores);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10_000),
                new ThreadFactoryBuilder().threadNamePrefix("dynamodb-async-persistence").build());

        executor.allowCoreThreadTimeOut(true);

        return executor;
    }

    @Bean
    public StaticCredentialsProvider awsCredentialsProvider() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accesskey, secretkey);
        return StaticCredentialsProvider.create(awsCreds);
    }

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(StaticCredentialsProvider awsCredentialsProvider,
                                                          ThreadPoolExecutor dynamoDBExecutor) {

        var endpointUrl = String.format("http://localhost:%d", dynamoDb.getFirstMappedPort());

        return DynamoDbAsyncClient.builder()
                .region(Region.of(region))
                .credentialsProvider(awsCredentialsProvider)
                .endpointOverride(URI.create(endpointUrl))
//                .endpointOverride(URI.create(endpoint))
                .asyncConfiguration(conf -> conf.advancedOption(FUTURE_COMPLETION_EXECUTOR, dynamoDBExecutor))
                .build();
    }

    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient dynamoDbAsyncClient) {
        return DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(dynamoDbAsyncClient)
                .build();
    }
}