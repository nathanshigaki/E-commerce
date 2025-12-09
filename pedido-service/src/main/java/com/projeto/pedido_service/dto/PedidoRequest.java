package com.projeto.pedido_service.dto;

import java.math.BigDecimal;

import com.projeto.pedido_service.model.Pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PedidoRequest(
    
    @NotBlank(message = "O pedido deve ter skucode.")
    String skucode,

    @NotNull(message = "O produto deve ter preço mairo que zero.")
    @PositiveOrZero(message = "O preço deve ser maior que zero.")
    BigDecimal preco, 
    
    @NotNull(message = "O produto deve ter preço mairo que zero.")
    @PositiveOrZero(message = "O preço deve ser maior que zero.")
    Integer quantidade) {

    public Pedido toPedido(){
        Pedido pedido = new Pedido();
        pedido.setSkucode(this.skucode);
        pedido.setPreco(this.preco);
        pedido.setQuantidade(this.quantidade);
        return pedido;
    }
}
