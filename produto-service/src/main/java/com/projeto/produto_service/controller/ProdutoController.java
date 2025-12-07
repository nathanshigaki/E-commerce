package com.projeto.produto_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public ResponseEntity<List<Produto>> getAllProdutos(){
        List<Produto> produtos = produtoService.getAllProdutos();
        if(produtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable String id){
        try {
            Produto existeProduto = produtoService.findById(id);
            return ResponseEntity.ok(existeProduto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Produto> updateProduto(@PathVariable String id, @RequestBody ProdutoDto produtoDto){
        try{
            Produto atualizadoProduto = produtoService.updateProduto(id, produtoDto);
            return ResponseEntity.ok(atualizadoProduto);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> deleteProduto(@PathVariable String id){
        try {
            produtoService.deleteProduto(id);
            return ResponseEntity.noContent().build(); 
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
