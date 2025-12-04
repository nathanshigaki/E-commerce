package com.projeto.pedido_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.pedido_service.dto.PedidoDto;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;
import com.projeto.pedido_service.service.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Test
    void testCreatePedido_Success(){
        PedidoDto pedidoDto = new PedidoDto("SKU123", new BigDecimal(120.00), 2);
        Pedido pedidoSalvo = new Pedido(1L, UUID.randomUUID().toString(), pedidoDto.skucode(), pedidoDto.preco(), pedidoDto.quantidade());

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        Pedido resultado = pedidoService.createPedido(pedidoDto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("SKU123", resultado.getSkucode());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testCreatePedido_Fail_NegativePrice(){
        PedidoDto pedidoInvalido = new PedidoDto("SKU456", new BigDecimal("-50.00"), 1);

        var exception = assertThrows(IllegalArgumentException.class, () -> pedidoService.createPedido(pedidoInvalido));
        assertEquals("O preço não pode ser nulo ou negativo.", exception.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testFindById_Success() throws Exception{
        Pedido pedidoExistente = new Pedido(1L, UUID.randomUUID().toString(), "SKU789", new BigDecimal(20.00), 27);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));

        Pedido resultado = pedidoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("SKU789", resultado.getSkucode());
    }

    @Test
    void testFindById_Fail(){
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var exception =assertThrows(Exception.class, () -> pedidoService.findById(idInexistente));
        assertEquals("Pedido não encontrado", exception.getMessage());
    }

    @Test
    void testDeletePedido_Success() throws Exception{
        Long idExistente = 1L;
        Pedido pedidoExistente = new Pedido(idExistente, UUID.randomUUID().toString(), "SKU789", new BigDecimal(20.00), 27);

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoExistente));

        pedidoService.deletePedido(idExistente);
        verify(pedidoRepository, times(1)).findById(idExistente);
        verify(pedidoRepository, times(1)).delete(pedidoExistente);
    }

    @Test
    void testDeletePedido_Fail(){
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        var exception =assertThrows(Exception.class, () -> pedidoService.deletePedido(idInexistente));
        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(pedidoRepository, never()).delete(any());
    }
}
