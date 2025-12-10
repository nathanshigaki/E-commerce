package com.projeto.inventario_service.Service;

import org.springframework.stereotype.Service;

import com.projeto.inventario_service.Repository.InventarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    public boolean isInStock(String skucode, Integer quantidade){
        return inventarioRepository.existsBySkucodeAndQuantidadeIsGreaterThanEqual(skucode, quantidade);
    }
}
