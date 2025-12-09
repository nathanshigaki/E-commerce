package com.projeto.produto_service.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoResponse;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.exception.ProdutoNotFoundException;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.repository.ProdutoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoResponse createProduto(ProdutoRequest produtoRequest){
        if (produtoRequest.preco() == null || produtoRequest.preco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }
        
        Produto produtoSalvar = produtoRequest.toProduto();
        Produto produtoSalvo = produtoRepository.save(produtoSalvar);
        return ProdutoResponse.fromProduto(produtoSalvo);
    }

    public List<ProdutoResponse> getAllProdutos(){
        return produtoRepository.findAll()
                .stream()
                .map(ProdutoResponse::fromProduto)
                .toList();
    }

    public ProdutoResponse findById(String id){
        return produtoRepository.findById(id)
                .map(ProdutoResponse::fromProduto)
                .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado."));
    }

    public ProdutoResponse updateProduto(String id, ProdutoUpdateDto updateDto){
        ProdutoResponse produtoExiste = findById(id);
        Produto produtoUpdate = ProdutoResponse.fromResponse(produtoExiste);

        if (updateDto.getNome() != null) produtoUpdate.setNome(updateDto.getNome());
        if (updateDto.getDescricao() != null) produtoUpdate.setDescricao(updateDto.getDescricao());
        
        if (updateDto.getPreco() != null) {
            if (updateDto.getPreco().compareTo(BigDecimal.ZERO) >= 0) {
                produtoUpdate.setPreco(updateDto.getPreco());
            } else {
                throw new IllegalArgumentException("O preço deve ser maior que zero.");
            }
        }

        Produto produtoSalvo = produtoRepository.save(produtoUpdate);
        return ProdutoResponse.fromProduto(produtoSalvo);
    }

    public void deleteProduto(String id){
        ProdutoResponse produtoExiste = findById(id);
        Produto apagarProduto = ProdutoResponse.fromResponse(produtoExiste);        
        produtoRepository.delete(apagarProduto);
    }
}
