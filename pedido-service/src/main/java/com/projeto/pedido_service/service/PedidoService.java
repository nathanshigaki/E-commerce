package com.projeto.pedido_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.projeto.pedido_service.dto.PedidoRequest;
import com.projeto.pedido_service.dto.PedidoResponse;
import com.projeto.pedido_service.exception.PedidoNotFoundException;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public PedidoResponse createPedido(PedidoRequest pedidoRequest){
        if (pedidoRequest.preco() == null || pedidoRequest.preco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        Pedido pedidoSalvar = pedidoRequest.toPedido();
        pedidoSalvar.setNumeroPedido(UUID.randomUUID().toString());
        Pedido pedidoSalvo = pedidoRepository.save(pedidoSalvar);

        return PedidoResponse.fromPedido(pedidoSalvo);
    }

    @Transactional
    public List<PedidoResponse> getAllPedidos(){
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponse::fromPedido)
                .toList();
    }

    @Transactional
    public PedidoResponse findById(Long id) {
        return pedidoRepository.findById(id)
                .map(PedidoResponse::fromPedido)
                .orElseThrow(() -> new PedidoNotFoundException("Pedido não encontrado."));
    }

    @Transactional
    public void deletePedido(Long id) {
        PedidoResponse pedidoExiste = findById(id);
        Pedido apagarPedido = PedidoResponse.fromResponse(pedidoExiste);
        pedidoRepository.delete(apagarPedido);
    }
}
