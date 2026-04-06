package com.projeto.inventario_service.dto;

import com.projeto.inventario_service.model.Inventario;

public record InventarioResponse(
    Long id,
    String skucode,
    Integer quantidade) {

    public static InventarioResponse fromInventario(Inventario inventario) {
        return new InventarioResponse(
            inventario.getId(),
            inventario.getSkucode(),
            inventario.getQuantidade()
        );
    }

    public static Inventario fromResponse(InventarioResponse inventarioResponse){
        return new Inventario(
            inventarioResponse.id(),
            inventarioResponse.skucode(),
            inventarioResponse.quantidade()
        );
    }
}
