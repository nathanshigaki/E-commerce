package com.projeto.pedido_service.dto;

import java.math.BigDecimal;

import com.projeto.pedido_service.model.Pedido;

public record PedidoResponse(Long id, String numeroPedido, String skucode, BigDecimal preco, Integer quantidade) {

    public static PedidoResponse fromPedido(Pedido pedido){
        return new PedidoResponse(
            pedido.getId(), 
            pedido.getNumeroPedido(), 
            pedido.getSkucode(), 
            pedido.getPreco(), 
            pedido.getQuantidade());
    }

    public static Pedido fromResponse(PedidoResponse pedidorResponse){
        return new Pedido(
            pedidorResponse.id(),
            pedidorResponse.numeroPedido(),
            pedidorResponse.skucode(),
            pedidorResponse.preco(),
            pedidorResponse.quantidade()
        );
    }
}
