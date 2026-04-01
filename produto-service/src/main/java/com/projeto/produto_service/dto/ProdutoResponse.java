package com.projeto.produto_service.dto;

import java.math.BigDecimal;

import com.projeto.produto_service.model.Produto;

public record ProdutoResponse(String id, String skucode,String nome, String descricao, BigDecimal preco) {

    public static ProdutoResponse fromProduto(Produto produto) {
        return new ProdutoResponse(
            produto.getId(),
            produto.getSkucode(),
            produto.getNome(),
            produto.getDescricao(),
            produto.getPreco()
        );
    }

    public static Produto fromResponse(ProdutoResponse produtoResponse){
        return new Produto(
            produtoResponse.id(),
            produtoResponse.skucode(),
            produtoResponse.nome(),
            produtoResponse.descricao(),
            produtoResponse.preco()
        );
    }   
}
