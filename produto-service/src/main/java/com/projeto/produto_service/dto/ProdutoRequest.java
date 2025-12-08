package com.projeto.produto_service.dto;

import java.math.BigDecimal;

import com.projeto.produto_service.model.Produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProdutoRequest(
    
    @NotBlank(message = "O produto deve ter nome.")
    String nome, 

    String descricao, 

    @NotNull(message = "O produto deve ter preço mairo que zero.")
    @PositiveOrZero(message = "O preço deve ser maior que zero.")
    BigDecimal preco) {

    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setNome(this.nome);
        produto.setPreco(this.preco);
        produto.setDescricao(this.descricao);
        return produto;
    }
}
