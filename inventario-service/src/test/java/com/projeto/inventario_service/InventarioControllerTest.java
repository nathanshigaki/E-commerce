package com.projeto.inventario_service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.projeto.inventario_service.Repository.InventarioRepository;
import com.projeto.inventario_service.model.Inventario;

import io.restassured.RestAssured;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventarioControllerTest {

    @Container
    @ServiceConnection
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.4.7");

	@LocalServerPort
    private Integer port;

	@Autowired
	private InventarioRepository inventarioRepository;

	@BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        inventarioRepository.deleteAll();

		Inventario caneta = new Inventario();
		caneta.setSkucode("caneta_azul");
		caneta.setQuantidade(100);

		inventarioRepository.save(caneta);
    }

	@Test
	void shouldCheckIfIsInStock(){
		Boolean response = RestAssured.given()
                .when()
                .get("/api/inventario?skucode=caneta_azul&quantidade=1")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        assertTrue(response);

		Boolean negativeResponse = RestAssured.given()
                .when()
                .get("/api/inventario?skucode=caneta_azul&quantidade=1000")
                .then()
                .log().all()
                .statusCode(200)
                .extract().response().as(Boolean.class);
        assertFalse(negativeResponse);
	}
}
