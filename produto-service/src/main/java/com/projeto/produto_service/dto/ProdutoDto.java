package com.projeto.produto_service.dto;

import java.math.BigDecimal;

public record ProdutoDto(String id, String nome, String descricao, BigDecimal preco) {

}
