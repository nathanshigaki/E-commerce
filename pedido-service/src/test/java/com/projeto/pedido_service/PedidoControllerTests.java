package com.projeto.pedido_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PedidoControllerTests {
    
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void shouldSubmitPedido() {
        String submitOrderJson = """
                {
                    "skucode": "notebook_nitro_v15",
                    "preco": "4824.99",
                    "quantidade": 1
                }
                """;

        given()
            .contentType("application/json")
            .body(submitOrderJson)
            .when()
            .post("api/pedido")
            .then()
            .log().all()
            .statusCode(201)
            .body("id", notNullValue())
            .body("skucode", equalTo("notebook_nitro_v15"));
    }

    @Test
    void shouldFindPedidoById(){
        Long pedidoId = createOrderAndGetId("SKU-FIND-ME", "50.00");

        given()
            .when()
            .get("api/pedido/" + pedidoId)
            .then()
            .log().all()
            .statusCode(200)
            .body("id", equalTo(pedidoId.intValue()))
            .body("skucode", equalTo("SKU-FIND-ME"));
    }

    @Test
    void shouldReturnAllPedidos(){
        createOrderAndGetId("SKU-LIST-1", "10.00");
        createOrderAndGetId("SKU-LIST-2", "20.00");

        given()
        .when()
        .get("api/pedido")
        .then()
        .log().all()
        .statusCode(200)
        .body("skucode", hasItems("SKU-LIST-1", "SKU-LIST-2"));
    }

    @Test
    void shouldDeletePedido(){
        Long pedidoId = createOrderAndGetId("SKU-DELETE-ME", "5.00");

        given()
            .when()
            .delete("api/pedido/" + pedidoId)
            .then()
            .log().all()
            .statusCode(204);

        given()
            .when()
            .get("/pedidos/" + pedidoId)
            .then()
            .log().all()
            .statusCode(404);
    }

    @Test
    void shouldReturn404ForNonExistentPedido(){
        given()
        .when()
            .get("/pedidos/999999") 
        .then()
            .log().all()
            .statusCode(404);
    }

    private Long createOrderAndGetId(String skucode, String preco) {
        String json = String.format("""
                {
                  "skucode": "%s",
                  "preco": "%s",
                  "quantidade": 1
                }
                """, skucode, preco);

        return given()
                .contentType(ContentType.JSON)
                .body(json)
                .when()
                .post("api/pedido")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath().getLong("id");
    }
}
