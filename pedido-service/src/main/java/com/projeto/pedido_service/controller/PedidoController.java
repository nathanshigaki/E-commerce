package com.projeto.pedido_service.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.projeto.pedido_service.dto.PedidoDto;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.service.PedidoService;

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
    public ResponseEntity<?> createPedido(@RequestBody PedidoDto pedidoDto) { 
        try {
            Pedido novoPedido = pedidoService.createPedido(pedidoDto);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest() 
                    .path("/{id}") 
                    .buildAndExpand(novoPedido.getId()) 
                    .toUri();

            return ResponseEntity.created(location).body(novoPedido);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getAllPedidos(){
        List<Pedido> pedidos = pedidoService.getAllPedidos();
        if (pedidos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Long id){
        try {
            Pedido existePedido = pedidoService.findById(id);
            return ResponseEntity.ok(existePedido);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Pedido> deletePedido(@PathVariable Long id){
        try {
            pedidoService.deletePedido(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
