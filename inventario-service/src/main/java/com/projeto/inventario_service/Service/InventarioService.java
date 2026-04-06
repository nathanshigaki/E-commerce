package com.projeto.inventario_service.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.projeto.inventario_service.Repository.InventarioRepository;
import com.projeto.inventario_service.dto.InventarioRequest;
import com.projeto.inventario_service.dto.InventarioResponse;
import com.projeto.inventario_service.model.Inventario;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository inventarioRepository;

    @Transactional
    public boolean isInStock(String skucode, Integer quantidade){
        return inventarioRepository.existsBySkucodeAndQuantidadeIsGreaterThanEqual(skucode, quantidade);
    }

    @Transactional
    public InventarioResponse createInventario(InventarioRequest inventarioRequest) {
        Inventario inventarioSalvar = inventarioRequest.toInventario();
        Inventario inventarioSalvo = inventarioRepository.save(inventarioSalvar);
        return InventarioResponse.fromInventario(inventarioSalvo);
    }

    @Transactional
    public List<InventarioResponse> getAllInventarios() {
        return inventarioRepository.findAll()
                .stream()
                .map(InventarioResponse::fromInventario)
                .toList();
    }

    @Transactional
    public InventarioResponse updateInventario(Long id, InventarioRequest updateDto) {
        InventarioResponse inventarioExiste = inventarioRepository.findById(id)
                .map(InventarioResponse::fromInventario)
                .orElseThrow(() -> new RuntimeException("Inventário não encontrado."));
        Inventario inventarioUpdate = InventarioResponse.fromResponse(inventarioExiste);
        if (updateDto.skucode() != null) {
            inventarioUpdate.setSkucode(updateDto.skucode());
        }

        if (updateDto.quantidade() != null) {
            if (updateDto.quantidade() >= 0) {
                inventarioUpdate.setQuantidade(updateDto.quantidade());
            } else {
                throw new IllegalArgumentException("A quantidade deve ser maior ou igual a zero.");
            }
        }
        Inventario inventarioSalvo = inventarioRepository.save(inventarioUpdate);
        return InventarioResponse.fromInventario(inventarioSalvo);
    }

    @Transactional
    public void deleteInventario(Long id) {
        if (!inventarioRepository.existsById(id)) {
            throw new RuntimeException("Inventário não encontrado.");
        }
        inventarioRepository.deleteById(id);
    }
}
