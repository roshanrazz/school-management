package com.roshan.gateway_service.config;


import com.roshan.gateway_service.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {


    private final RouteValidator routeValidator;
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (!routeValidator.isSecured.test(request)) {
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            log.info("No authorization header found");
            return unauthorized(exchange);
        }

        try {
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String authToken = authHeader.substring(7);

                Claims claims = jwtUtils.extractClaims(authToken);
                ArrayList<String> roles = claims.get("roles", ArrayList.class);
                String username = jwtUtils.extractUsername(authToken);

                if (!jwtUtils.validateToken(authToken, username)) {
                    unauthorized(exchange);
                }
                request.mutate().header("X-Auth-User", username);
                request.mutate().header("X-User-Role", roles.get(0));
                log.info("Authenticated user headers: " + request.getHeaders());
                return chain.filter(exchange);
            }
        } catch (Exception ex) {
            log.warn("Failed to authenticate user: {}", ex.getMessage());
        }

        return unauthorized(exchange);
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
