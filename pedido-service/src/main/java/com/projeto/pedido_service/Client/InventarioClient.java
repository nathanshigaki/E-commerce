package com.projeto.pedido_service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventarioClient {

    @GetExchange("/api/inventario")
    boolean isInStock(@RequestParam String skucode, @RequestParam Integer quantidade);
}
