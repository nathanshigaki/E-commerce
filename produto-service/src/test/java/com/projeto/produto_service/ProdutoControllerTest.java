package com.projeto.produto_service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.repository.ProdutoRepository;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoControllerTest {

	@Container
	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.0");

	@LocalServerPort
    private Integer port;

	@Autowired
	private ProdutoRepository produtoRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

		produtoRepository.deleteAll();
    }

	@Test
	void shouldCreateProduto(){
		ProdutoRequest produtoRequest = getProdutoRequest();

		Response response = RestAssured.given()
			.contentType("application/json")
			.body(produtoRequest)
			.when()	
			.post("api/produto")
			.then()
			.log().all()
			.extract().response();
			
		assertEquals(201, response.statusCode());
		assertEquals(produtoRequest.nome(), response.jsonPath().getString("nome"));
		assertEquals(produtoRequest.descricao(), response.jsonPath().getString("descricao"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
		assertEquals(0, produtoRequest.preco().compareTo(precoRetornado));

		assertEquals(1, produtoRepository.count());
        
        Produto produtoSalvo = produtoRepository.findAll().get(0);
        assertEquals(produtoRequest.nome(), produtoSalvo.getNome());
	}
	@Test
	void shouldGetAllProduto(){
		Produto p1 = getProduto("Teclado", "Teclado mecanico", new BigDecimal(300));
		Produto p2 = getProduto("Mouse", "Logitech", new BigDecimal(120));

		produtoRepository.saveAll(List.of(p1, p2));

		Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.get("api/produto") 
			.then()
			.log().all() 
			.extract().response();

		assertEquals(200, response.statusCode());
		assertEquals(2, response.jsonPath().getList("$").size());
		assertEquals("Teclado", response.jsonPath().getString("[0].nome"));
    	assertEquals("Mouse", response.jsonPath().getString("[1].nome"));
	}

	@Test
	void shouldFindById(){
		Produto produto = getProduto("Monitor", "240hz", new BigDecimal(420));
		produto = produtoRepository.save(produto);
		String id = produto.getId();

		Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.get("api/produto/{id}", id)
			.then()
			.log().all()
			.extract().response();

		assertEquals(200, response.statusCode());
		assertEquals(id, response.jsonPath().getString("id"));
		assertEquals(produto.getNome(), response.jsonPath().getString("nome"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
		assertEquals(0, produto.getPreco().compareTo(precoRetornado));
	}

	@Test
	void shouldUpdateProduto(){
		Produto produto = getProduto("Cadeira", "Simples", new BigDecimal(420));
		produto = produtoRepository.save(produto);

		ProdutoUpdateDto updateDto = new ProdutoUpdateDto();
        updateDto.setPreco(new BigDecimal(800)); 
        updateDto.setNome("Cadeira Gamer");

		Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.body(updateDto)
			.when()
			.patch("api/produto/{id}", produto.getId())
			.then()
			.log().all()
			.extract().response();

		assertEquals(200, response.statusCode());
    	assertEquals("Cadeira Gamer", response.jsonPath().getString("nome"));

		Produto produtoAtualizado = produtoRepository.findById(produto.getId()).orElseThrow();
        assertEquals(0, new BigDecimal(800).compareTo(produtoAtualizado.getPreco()));
	}

	@Test
	void shouldDeleteProduto(){
		Produto produto = getProduto("Mesa", "Simples", new BigDecimal(230));
		produto = produtoRepository.save(produto);

		RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.delete("api/produto/{id}", produto.getId())
			.then()
			.statusCode(204);
		
		Optional<Produto> busca = produtoRepository.findById(produto.getId());
        assertFalse(busca.isPresent());
	}

	private ProdutoRequest getProdutoRequest(){
		return new ProdutoRequest("Caneta", "Caneta azul", new BigDecimal(4));
	}

	private Produto getProduto(String nome, String descricao, BigDecimal preco){
		return Produto.builder().nome(nome).descricao(descricao).preco(preco).build();
	}
}