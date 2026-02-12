package com.projeto.api_gateway.rotas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
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
    public RouterFunction<ServerResponse> pedidoServiceRota(){
        return route("pedido_service")
            .GET("/api/pedido/**", http())
            .before(uri("http://localhost:8081"))                
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventarioServiceRota(){
        return route("inventario_service")
            .GET("/api/inventario/**", http())
            .before(uri("http://localhost:8082"))                
            .build();
    }
}
