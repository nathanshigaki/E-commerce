package com.projeto.produto_service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.repository.ProdutoRepository;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ProdutoControllerTest {

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
		Produto p1 = Produto.builder().nome("Teclado").descricao("Teclado mecanico").preco(new BigDecimal(300)).build();
		Produto p2 = Produto.builder().nome("Mouse").descricao("Logitech").preco(new BigDecimal(120)).build();

		produtoRepository.saveAll(List.of(p1, p2));

		RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.get("api/produto") 
			.then()
			.log().all() 
			.statusCode(200) 
			.body("size()", Matchers.is(2)) 
			.body("[0].nome", Matchers.equalTo("Teclado"))
			.body("[1].nome", Matchers.equalTo("Mouse"));
	}

	@Test
	void shouldFindById(){
		Produto produto = Produto.builder().nome("Monitor").descricao("240hz").preco(new BigDecimal(420)).build();
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
		Produto produto = Produto.builder().nome("Cadeira").descricao("Simples").preco(new BigDecimal(420)).build();
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
		Produto produto = Produto.builder().nome("Mesa").descricao("Simples").preco(new BigDecimal(230)).build();
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

	public ProdutoRequest getProdutoRequest(){
		return new ProdutoRequest("Caneta", "Caneta azul", new BigDecimal(4));
	}
}
