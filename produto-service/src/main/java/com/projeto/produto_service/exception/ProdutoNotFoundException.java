package com.projeto.produto_service.exception;

public class ProdutoNotFoundException extends RuntimeException{
    public ProdutoNotFoundException(String message){
        super(message);
    }
}
