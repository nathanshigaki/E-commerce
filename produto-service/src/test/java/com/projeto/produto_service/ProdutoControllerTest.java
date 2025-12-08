package com.projeto.produto_service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoResponse;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.service.ProdutoService;

import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoControllerTest {

	@LocalServerPort
    private Integer port;

	@MockitoBean
	private ProdutoService produtoService;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

	@Test
	void shouldCreateProduto(){
		ProdutoRequest produtoRequest = getProdutoRequest();
		ProdutoResponse produtoResponse = getProdutoResponse(produtoRequest);

		Mockito.when(produtoService.createProduto(produtoRequest)).thenReturn(produtoResponse);

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
	}

	@Test
	void shouldGetAllProduto(){
		ProdutoResponse p1 = new ProdutoResponse("123", "Teclado", "Teclado mecanico", new BigDecimal(300));
		ProdutoResponse p2 = new ProdutoResponse("456", "Mouse", "Logitech", new BigDecimal(120));

		List<ProdutoResponse> listaProdutoResponse = List.of(p1, p2);

		Mockito.when(produtoService.getAllProdutos()).thenReturn(listaProdutoResponse);

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
		ProdutoRequest produtoRequest = getProdutoRequest();
		ProdutoResponse produtoResponse = getProdutoResponse(produtoRequest);

		Mockito.when(produtoService.findById(produtoResponse.id())).thenReturn(produtoResponse);

		Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.get("api/produto/{id}", "a12bc3")
			.then()
			.log().all()
			.extract().response();

		assertEquals(200, response.statusCode());
		assertEquals("a12bc3", response.jsonPath().getString("id"));
		assertEquals(produtoRequest.nome(), response.jsonPath().getString("nome"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
		assertEquals(0, produtoRequest.preco().compareTo(precoRetornado));
	}

	@Test
	void shouldUpdateProduto(){
		ProdutoUpdateDto updateDto = new ProdutoUpdateDto();
		updateDto.setPreco(new BigDecimal(150));

		String id = "a12bc3";
		ProdutoRequest produtoRequest = getProdutoRequest();
		ProdutoResponse produtoResponse = new ProdutoResponse(
				id, produtoRequest.nome(), 
				produtoRequest.descricao(), 
				new BigDecimal(150));

		Mockito.when(produtoService.updateProduto(id, updateDto)).thenReturn(produtoResponse);

		Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.body(updateDto)
			.when()
			.patch("api/produto/{id}", "a12bc3")
			.then()
			.log().all()
			.extract().response();

		assertEquals(200, response.statusCode());
    	assertEquals("Caneta", response.jsonPath().getString("nome"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
    	assertEquals(0, new BigDecimal("150.00").compareTo(precoRetornado));
	}

	@Test
	void shouldDeleteProduto(){
		Mockito.doNothing().when(produtoService).deleteProduto("a12bc3");

		RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.delete("api/produto/{id}", "a12bc3")
			.then()
			.statusCode(204);
	}

	public ProdutoRequest getProdutoRequest(){
		return new ProdutoRequest("Caneta", "Caneta azul", new BigDecimal(4));
	}

	public ProdutoResponse getProdutoResponse(ProdutoRequest produtoRequest){
		String id = "a12bc3";
		return new ProdutoResponse(id, produtoRequest.nome(), produtoRequest.descricao(), produtoRequest.preco());
	}
}
