package com.projeto.pedido_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

import com.projeto.pedido_service.dto.PedidoRequest;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class PedidoControllerTests {
    
    @LocalServerPort
    private Integer port;

    @Autowired
    private PedidoRepository pedidoRepository;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        pedidoRepository.deleteAll();
    }

    @Test
    void shouldCreatePedido() {
        PedidoRequest pedidoRequest = getPedidoRequest();

        Response response = RestAssured.given()
            .contentType("application/json")
            .body(pedidoRequest)
            .when()
            .post("api/pedido")
            .then()
            .log().all()
            .extract().response();
        
        assertEquals(201, response.statusCode());
		assertEquals(pedidoRequest.skucode(), response.jsonPath().getString("skucode"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
		assertEquals(0, pedidoRequest.preco().compareTo(precoRetornado));
        assertEquals(pedidoRequest.quantidade(), response.jsonPath().getInt("quantidade"));

		assertEquals(1, pedidoRepository.count());
        
        Pedido produtoSalvo = pedidoRepository.findAll().get(0);
        assertEquals(pedidoRequest.skucode(), produtoSalvo.getSkucode());
    }

    @Test
    void shouldGetAllPedido(){
        Pedido p1 = getPedido("Pedido 1", "SKU111", new BigDecimal(100), 1);
        Pedido p2 = getPedido("Pedido 2", "SKU222", new BigDecimal(200), 2);

        pedidoRepository.saveAll(List.of(p1, p2));

        Response response = RestAssured.given()
			.contentType(ContentType.JSON)
			.when()
			.get("api/pedido") 
			.then()
			.log().all() 
			.extract().response();

		assertEquals(200, response.statusCode());
		assertEquals(2, response.jsonPath().getList("$").size());
		assertEquals("Pedido 1", response.jsonPath().getString("[0].numeroPedido"));
    	assertEquals("Pedido 2", response.jsonPath().getString("[1].numeroPedido"));
    }

    @Test
    void shouldFindPedidoById(){
        Pedido pedido = getPedido("Pedido 1", "SKU111", new BigDecimal(100), 1);
        pedido = pedidoRepository.save(pedido);
        Long id = pedido.getId();

        Response response = RestAssured.given()
            .contentType(ContentType.JSON)
            .when()
            .get("api/pedido/" + id)
            .then()
            .log().all()
            .extract().response();
        
        assertEquals(200, response.statusCode());
		assertEquals(id, response.jsonPath().getLong("id"));
		assertEquals(pedido.getNumeroPedido(), response.jsonPath().getString("numeroPedido"));
        assertEquals(pedido.getSkucode(), response.jsonPath().getString("skucode"));

		BigDecimal precoRetornado = response.jsonPath().getObject("preco", BigDecimal.class);
		assertEquals(0, pedido.getPreco().compareTo(precoRetornado));

        assertEquals(pedido.getQuantidade(), response.jsonPath().getInt("quantidade"));
    }

    @Test
    void shouldDeletePedido(){
        Pedido pedido = getPedido("Pedido 1", "SKU111", new BigDecimal(100), 1);
        pedido = pedidoRepository.save(pedido);

        RestAssured.given()
            .contentType(ContentType.JSON)
            .when()
            .delete("api/pedido/{id}", pedido.getId());

        Optional<Pedido> busca = pedidoRepository.findById(pedido.getId());
        assertFalse(busca.isPresent());
    }

    private PedidoRequest getPedidoRequest(){
        return new PedidoRequest("SKU-123", new BigDecimal(65), 1);
    }

    private Pedido getPedido(String numeroPedido, String skucode, BigDecimal preco, int quantidade ){
        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(numeroPedido);
        pedido.setSkucode(skucode);
        pedido.setPreco(preco);
        pedido.setQuantidade(quantidade);
        return pedido;
    }
}
