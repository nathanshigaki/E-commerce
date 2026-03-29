package com.projeto.produto_service.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI produtoServiceAPI(){
        return new OpenAPI()
                .info(new Info().title("Produto Service API")
                    .description("Rest API do Produto Service")
                    .version("v0.0.1")
                    .license(new License().name("Apache 2.0")));
    }
}
