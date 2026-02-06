package com.projeto.produto_service.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.projeto.produto_service.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String>{
    
    Optional<Produto> findByNome(String nome);
}
