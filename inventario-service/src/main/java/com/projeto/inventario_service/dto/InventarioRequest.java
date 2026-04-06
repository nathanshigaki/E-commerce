package com.projeto.inventario_service.dto;

import com.projeto.inventario_service.model.Inventario;

public record InventarioRequest(String skucode, Integer quantidade) {

    public Inventario toInventario() {
        Inventario inventario = new Inventario();
        inventario.setSkucode(this.skucode);
        inventario.setQuantidade(this.quantidade);
        return inventario;
    }
}
