package com.projeto.api_gateway.rotas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class Rotas {

    @Bean
    public RouterFunction<ServerResponse> produtoServiceRota(){
        return route("produto_service")
            .route(RequestPredicates.path("/api/produto/**"), http()) 
            .before(uri("http://localhost:8080"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("produtoServiceCircuitBreaker", URI.create("forward:/fallbackRoute")))         
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> produtoServiceSwaggerRota(){
        return route("produto_service_swagger")
            .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), http()) 
            .before(uri("http://localhost:8080"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("produtoServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute"))) // CORREÇÃO AQUI
            .before(setPath("/api-docs"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pedidoServiceRota(){
        return route("pedido_service")
            .route(RequestPredicates.path("/api/pedido/**"), http()) 
            .before(uri("http://localhost:8081"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("pedidoServiceCircuitBreaker", URI.create("forward:/fallbackRoute"))) // CORREÇÃO AQUI                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pedidoServiceSwaggerRota(){
        return route("pedido_service_swagger")
            .route(RequestPredicates.path("/aggregate/pedido-service/v3/api-docs"), http()) 
            .before(uri("http://localhost:8081"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("pedidoServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute"))) // CORREÇÃO AQUI
            .before(setPath("/api-docs"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioServiceRota(){
        return route("inventario_service")
            .route(RequestPredicates.path("/api/inventario/**"), http()) 
            .before(uri("http://localhost:8082")) 
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventarioServiceCircuitBreaker", URI.create("forward:/fallbackRoute"))) // CORREÇÃO AQUI               
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioServiceSwaggerRota(){
        return route("inventario_service_swagger")
            .route(RequestPredicates.path("/aggregate/inventario-service/v3/api-docs"), http())
            .before(uri("http://localhost:8082"))
            .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventarioServiceSwaggerCircuitBreaker", URI.create("forward:/fallbackRoute"))) // CORREÇÃO AQUI
            .before(setPath("/api-docs"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
            .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
            .body("Serviço indisponível no momento. Por favor, tente novamente mais tarde."))
            .build();
    }
}
