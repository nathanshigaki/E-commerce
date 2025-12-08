package com.projeto.produto_service.dto;

import java.math.BigDecimal;

import com.projeto.produto_service.model.Produto;

public record ProdutoRequest(String nome, String descricao, BigDecimal preco) {

    public Produto toProduto() {
        Produto produto = new Produto();
        produto.setNome(this.nome);
        produto.setPreco(this.preco);
        produto.setDescricao(this.descricao);
        return produto;
    }
}
