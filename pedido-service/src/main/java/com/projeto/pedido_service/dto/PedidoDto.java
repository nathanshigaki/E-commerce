package com.projeto.pedido_service.dto;

import java.math.BigDecimal;

public record PedidoDto(String skucode, BigDecimal preco, Integer quantidade) {

}
