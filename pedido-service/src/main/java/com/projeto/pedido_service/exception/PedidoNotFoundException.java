package com.projeto.pedido_service.exception;

public class PedidoNotFoundException extends RuntimeException{
    public PedidoNotFoundException(String message){
        super(message);
    }
}
