package com.example.dynamoDemo.configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@EnableWebFlux
public class WebFluxConfiguration {

  @Bean
  RouterFunction<ServerResponse> routerFunction() {
    return  route(GET("/"), req ->
        ServerResponse.temporaryRedirect(URI.create("/swagger-ui-custom.html"))
            .build());
  }
}