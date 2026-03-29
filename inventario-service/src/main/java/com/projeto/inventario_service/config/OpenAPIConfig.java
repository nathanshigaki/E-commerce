package com.projeto.inventario_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventarioServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("Inventário Service API")
                    .description("Rest API do inventário Service")
                    .version("v0.0.1")
                    .license(new License().name("Apache 2.0")));
    }
}
