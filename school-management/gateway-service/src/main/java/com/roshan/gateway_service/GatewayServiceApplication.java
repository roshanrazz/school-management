package com.roshan.gateway_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r
                        .path("/api/v1/users/**","/api/v1/login","/api/v1/token/refresh","/api/v1/register/**")
                        .uri("lb://USER-SERVICE"))

                .route("assignment-service", r -> r
                        .path("/api/v1/assignments/**")
                        .uri("lb://ASSIGNMENT-SERVICE"))
                .build();
    }

}
