package com.example.dynamoDemo.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.utils.ThreadFactoryBuilder;

import java.net.URI;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR;

@Configuration
public class DynamoDbConfiguration {

  public static final String COUNTRY_INDEX = "Country-index";

  @Value("${amazon.dynamodb.endpoint}")
  private String endpoint;
  @Value("${amazon.aws.accesskey}")
  private String accesskey;
  @Value("${amazon.aws.secretkey}")
  private String secretkey;
  @Value("${amazon.aws.region}")
  private String region;

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
    return DynamoDbAsyncClient.builder()
            .region(Region.of(region))
            .credentialsProvider(awsCredentialsProvider)
            .endpointOverride(URI.create(endpoint))
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