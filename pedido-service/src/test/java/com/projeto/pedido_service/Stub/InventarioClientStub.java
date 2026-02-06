package com.projeto.pedido_service.Stub;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class InventarioClientStub {

    public static void stubInventoryCall(String skucode, Integer quantidade){
        stubFor(get(urlEqualTo("/api/inventario?skucode=" + skucode + "&quantidade=" + quantidade))
                .willReturn(aResponse()
                    .withStatus(200)
                    .withHeader("Content-Type", "application/json")
                    .withBody("true")));
    }
}
