package com.roshan.gateway_service.config;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> ALLOWED_PATHS = List.of(
            "/api/v1/login",
            "/api/v1/token/refresh"
    );
    public Predicate<ServerHttpRequest> isSecured =
            request -> ALLOWED_PATHS.stream()
                    .noneMatch(uri -> uri.equals(request.getURI().getPath()));
}
