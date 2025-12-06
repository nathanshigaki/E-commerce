package com.projeto.produto_service;

import org.springframework.boot.SpringApplication;

public class TestProdutoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ProdutoServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
