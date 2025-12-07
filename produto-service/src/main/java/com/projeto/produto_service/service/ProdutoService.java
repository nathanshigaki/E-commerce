package com.projeto.produto_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.produto_service.dto.ProdutoDto;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto createProduto(ProdutoDto produtoDto){

        if (produtoDto.nome() == null){
            throw new IllegalArgumentException("O produto deve ter um nome");
        } 
        
        if (produtoDto.preco() == null || produtoDto.preco().compareTo(BigDecimal.ZERO)<0) {
            throw new IllegalArgumentException("O preço não pode ser nulo ou negativo.");
        }

        Produto produto = Produto.builder()
                .nome(produtoDto.nome())
                .descricao(produtoDto.descricao())
                .preco(produtoDto.preco())
                .build();
        
        return produtoRepository.save(produto);
    }

    public List<Produto> getAllProdutos(){
        return produtoRepository.findAll();
    }

    public Produto findById(String id){
        return produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    public Produto updateProduto(String id, ProdutoUpdateDto updateDto){
        Produto produtoExistente = findById(id);

        if (updateDto.getNome() != null) produtoExistente.setNome(updateDto.getNome());
        if (updateDto.getDescricao() != null) produtoExistente.setDescricao(updateDto.getDescricao());
        
        if (updateDto.getPreco() != null) {
            if (updateDto.getPreco().compareTo(BigDecimal.ZERO) > 0) {
                produtoExistente.setPreco(updateDto.getPreco());
            } else {
                throw new IllegalArgumentException("O preço não pode ser negativo.");
            }
        }

        return produtoRepository.save(produtoExistente);
    }

    public void deleteProduto(String id){
        Produto apagarProduto = findById(id);
        produtoRepository.delete(apagarProduto);
    }
}
