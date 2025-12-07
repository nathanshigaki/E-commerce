package com.projeto.pedido_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projeto.pedido_service.dto.PedidoDto;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public Pedido createPedido(PedidoDto pedidoDto){

        if (pedidoDto.preco() == null || pedidoDto.preco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço não pode ser nulo ou negativo.");
        }
        
        if (pedidoDto.quantidade() == null || pedidoDto.quantidade() < 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        if (pedidoDto.skucode() == null || pedidoDto.skucode().isBlank()) {
            throw new IllegalArgumentException("O skucode não pode ser nulo ou vazio.");
        }

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(UUID.randomUUID().toString());
        pedido.setPreco(pedidoDto.preco());
        pedido.setSkucode(pedidoDto.skucode());
        pedido.setQuantidade(pedidoDto.quantidade());

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public List<Pedido> getAllPedidos(){
        return pedidoRepository.findAll();
    }

    @Transactional
    public Pedido findById(Long id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    @Transactional
    public void deletePedido(Long id) {
        Pedido apagarPedido = findById(id);
        pedidoRepository.delete(apagarPedido);
    }
}
