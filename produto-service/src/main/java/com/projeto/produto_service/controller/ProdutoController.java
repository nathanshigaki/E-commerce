package com.projeto.produto_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoResponse;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.service.ProdutoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> createProduto(@RequestBody @Valid ProdutoRequest produtoRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.createProduto(produtoRequest));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> getAllProdutos(){
        return ResponseEntity.ok(produtoService.getAllProdutos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable String id){
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProdutoResponse> updateProduto(@PathVariable String id, @RequestBody ProdutoUpdateDto updateDto){
        return ResponseEntity.ok(produtoService.updateProduto(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProdutoResponse> deleteProduto(@PathVariable String id){
        produtoService.deleteProduto(id);
        return ResponseEntity.noContent().build(); 
    }
}
