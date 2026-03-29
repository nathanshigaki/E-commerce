package com.projeto.api_gateway.rotas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class Rotas {

    @Bean
    public RouterFunction<ServerResponse> produtoServiceRota(){
        return route("produto_service")
            .GET("/api/produto/**", http())
            .before(uri("http://localhost:8080"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> produtoServiceSwaggerRota(){
        return route("produto_service_swagger")
            .GET("/aggregate/product-service/v3/api-docs", http())
            .before(uri("http://localhost:8080"))
            .before(setPath("/api-docs"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pedidoServiceRota(){
        return route("pedido_service")
            .GET("/api/pedido/**", http())
            .before(uri("http://localhost:8081"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pedidoServiceSwaggerRota(){
        return route("pedido_service_swagger")
            .GET("/aggregate/pedido-service/v3/api-docs", http())
            .before(uri("http://localhost:8081"))
            .before(setPath("/api-docs"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioServiceRota(){
        return route("inventario_service")
            .GET("/api/inventario/**", http())
            .before(uri("http://localhost:8082"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioServiceSwaggerRota(){
        return route("inventario_service_swagger")
            .GET("/aggregate/inventario-service/v3/api-docs", http())
            .before(uri("http://localhost:8082"))
            .before(setPath("/api-docs"))                
            .build();
    }
}
