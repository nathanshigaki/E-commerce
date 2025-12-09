package com.projeto.pedido_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.pedido_service.dto.PedidoRequest;
import com.projeto.pedido_service.dto.PedidoResponse;
import com.projeto.pedido_service.service.PedidoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> createPedido(@RequestBody @Valid PedidoRequest pedidoRequest) { 
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.createPedido(pedidoRequest));
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> getAllPedidos(){
        return ResponseEntity.ok(pedidoService.getAllPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(pedidoService.findById(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<PedidoResponse> deletePedido(@PathVariable Long id){
        pedidoService.deletePedido(id);
        return ResponseEntity.noContent().build();
    }
}
