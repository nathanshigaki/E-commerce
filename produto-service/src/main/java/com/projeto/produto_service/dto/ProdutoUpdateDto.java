package com.projeto.produto_service.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProdutoUpdateDto {

    private String nome;
    private String descricao;
    private BigDecimal preco;
}
