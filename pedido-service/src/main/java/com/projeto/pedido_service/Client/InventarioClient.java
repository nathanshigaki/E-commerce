package com.projeto.pedido_service.client;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

public interface InventarioClient {

    Logger log = LoggerFactory.getLogger(InventarioClient.class);

    @GetExchange("/api/inventario/check-stock")
    @CircuitBreaker(name = "inventario", fallbackMethod = "fallbackmethod")
    @Retry(name = "inventario")
    boolean isInStock(@RequestParam String skucode, @RequestParam Integer quantidade);

    @PutMapping("/api/inventario/decrement")
    @CircuitBreaker(name = "inventario", fallbackMethod = "fallbackDecrement")
    void decrementStock(@RequestParam String skucode, @RequestParam Integer quantidade);

    @PatchMapping("/api/inventario/skucode/{skucode}")
    void updateStock(@RequestParam String skucode, @RequestParam Integer quantidade);

    default boolean fallbackmethod(String skucode, Integer quantidade, Throwable throwable){
        log.error("Erro ao verificar o estoque para o produto {}: {}", skucode, throwable.getMessage());
        return false; 
    }

    default void fallbackDecrement(String skucode, Integer quantidade, Throwable throwable) {
        log.error("Falha ao baixar estoque para {}: {}", skucode, throwable.getMessage());
    }
}
