package com.projeto.produto_service.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projeto.produto_service.dto.ProdutoDto;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.service.ProdutoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<?> createProduto(@RequestBody ProdutoDto ProdutoDto){
        try{   
            Produto novoProduto = produtoService.createProduto(ProdutoDto);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novoProduto.getId())
                    .toUri();

            return ResponseEntity.created(location).body(novoProduto);
        } catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
