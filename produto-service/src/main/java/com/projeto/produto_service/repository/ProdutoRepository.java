package com.projeto.produto_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.projeto.produto_service.model.Produto;

public interface ProdutoRepository extends MongoRepository<Produto, String>{

}
