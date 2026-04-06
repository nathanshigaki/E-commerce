package com.projeto.produto_service.dto;

import java.math.BigDecimal;

import com.projeto.produto_service.model.Produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdutoRequest(
    
    String id,

    @NotBlank(message = "O produto deve ter nome.")
    String nome, 

    String descricao, 
    String skucode,
    @NotNull(message = "O produto deve ter preço mairo que zero.")
    @PositiveOrZero(message = "O preço deve ser maior que zero.")
    BigDecimal preco) {

    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setId(this.id);
        produto.setNome(this.nome);
        produto.setDescricao(this.descricao);
        produto.setSkucode(this.skucode);
        produto.setPreco(this.preco);
        return produto;
    }
}
