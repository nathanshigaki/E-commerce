package com.projeto.pedido_service.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.projeto.pedido_service.client.InventarioClient;
import com.projeto.pedido_service.dto.PedidoRequest;
import com.projeto.pedido_service.dto.PedidoResponse;
import com.projeto.pedido_service.event.PedidoFeitoEvent;
import com.projeto.pedido_service.exception.PedidoNotFoundException;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final InventarioClient inventarioClient;
    private final KafkaTemplate<String, PedidoFeitoEvent> kafkaTemplate;

    @Transactional
    public PedidoResponse createPedido(PedidoRequest pedidoRequest){
        boolean isProdutoInStock = inventarioClient.isInStock(pedidoRequest.skucode(), pedidoRequest.quantidade());

        if (pedidoRequest.preco() == null || pedidoRequest.preco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço deve ser maior que zero.");
        }

        if (!isProdutoInStock){
            throw new IllegalArgumentException("Produto com skucode: "+ pedidoRequest.skucode() + " não tem no estoque.");
        }

        Pedido pedidoSalvar = pedidoRequest.toPedido();
        pedidoSalvar.setNumeroPedido(UUID.randomUUID().toString());
        Pedido pedidoSalvo = pedidoRepository.save(pedidoSalvar);

        PedidoFeitoEvent pedidoFeitoEvent = new PedidoFeitoEvent();
        pedidoFeitoEvent.setNumeroPedido(pedidoSalvo.getNumeroPedido());
        pedidoFeitoEvent.setEmail(pedidoRequest.userDetails().email());
        log.info("Enviando evento de pedido feito para Kafka: {}", pedidoFeitoEvent);
        kafkaTemplate.send("pedido-feito", pedidoFeitoEvent);
        log.info("Evento de pedido feito enviado para Kafka: {}", pedidoFeitoEvent);

        inventarioClient.decrementStock(pedidoRequest.skucode(), pedidoRequest.quantidade());

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
