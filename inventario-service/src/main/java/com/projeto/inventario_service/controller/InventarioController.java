package com.projeto.inventario_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.inventario_service.Service.InventarioService;
import com.projeto.inventario_service.dto.InventarioRequest;
import com.projeto.inventario_service.dto.InventarioResponse;
import com.projeto.inventario_service.model.Inventario;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @PostMapping
    public ResponseEntity<InventarioResponse> create(@RequestBody @Valid InventarioRequest inventarioRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventarioService.createInventario(inventarioRequest));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponse>> getAllInventario() {
        return ResponseEntity.ok(inventarioService.getAllInventarios());
    }

    @GetMapping("/detalhes")
    public Inventario getDetalhes(@RequestParam String skucode) {
        return inventarioService.getInventarioBySkucode(skucode);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<InventarioResponse> updateInventario(@PathVariable Long id, @RequestBody @Valid InventarioRequest updateDto) {
        return ResponseEntity.ok(inventarioService.updateInventario(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<InventarioResponse> deleteInventario(@PathVariable Long id) {
        inventarioService.deleteInventario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-stock")
    public boolean isInStock(@RequestParam String skucode, Integer quantidade){
        return inventarioService.isInStock(skucode, quantidade);
    }

    @PutMapping("/decrement")
    public void decrementStock(@RequestParam String skucode, @RequestParam Integer quantidade) {
        inventarioService.decrementStock(skucode, quantidade);
    }

    @PutMapping("/update-quantidade")
    public void updateQuantidade(@RequestParam String skucode, @RequestParam Integer quantidade) {
        inventarioService.updateQuantidadeManual(skucode, quantidade);
    }
}
