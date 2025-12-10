package com.projeto.inventario_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto.inventario_service.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long>{

    boolean existsBySkucodeAndQuantidadeIsGreaterThanEqual(String skucode, Integer quantidade);
}
