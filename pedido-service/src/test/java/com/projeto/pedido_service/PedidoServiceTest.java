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
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.pedido_service.Client.InventarioClient;
import com.projeto.pedido_service.dto.PedidoRequest;
import com.projeto.pedido_service.dto.PedidoResponse;
import com.projeto.pedido_service.exception.PedidoNotFoundException;
import com.projeto.pedido_service.model.Pedido;
import com.projeto.pedido_service.repository.PedidoRepository;
import com.projeto.pedido_service.service.PedidoService;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private InventarioClient inventarioClient;

    @Mock
    private PedidoRepository pedidoRepository;

    private Pedido pedido;
    private PedidoRequest pedidoRequest;

    @BeforeEach
    void setUp(){
        pedido = new Pedido(
            1L, 
            "a12bc3", 
            "SKU123", 
            new BigDecimal(140), 
            2
        );

        pedidoRequest = new PedidoRequest(
            pedido.getSkucode(), 
            pedido.getPreco(), 
            pedido.getQuantidade()
        );
    }

    @Test
    void testCreatePedido_Success(){
        when(inventarioClient.isInStock(pedidoRequest.skucode(), pedidoRequest.quantidade())).thenReturn(true);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        PedidoResponse resultado = pedidoService.createPedido(pedidoRequest);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("SKU123", resultado.skucode());
        assertEquals(new BigDecimal(140), resultado.preco());
        assertEquals(2, resultado.quantidade());
        verify(inventarioClient, times(1)).isInStock(any(), any());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void testCreatePedido_Fail_SemEstoque(){
        when(inventarioClient.isInStock(any(), any())).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.createPedido(pedidoRequest));
        
        assertEquals("Produto com skucode: SKU123 não tem no estoque.", exception.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testCreatePedido_Fail_PrecoInvalido(){
        PedidoRequest pedidoInvalido = new PedidoRequest("SKU456", new BigDecimal("-50.00"), 1);

        assertThrows(IllegalArgumentException.class, 
            () -> pedidoService.createPedido(pedidoInvalido));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void testGetAllPedido(){
        Pedido p1 = new Pedido(
            1L, 
            "Pedido 1", 
            "SKU123", 
            new BigDecimal(10), 
            1
        );

        Pedido p2 = new Pedido(
            2L, 
            "Pedido 2", 
            "SKU456", 
            new BigDecimal(20), 
            2
        );

        List<Pedido> listaPedidos = List.of(p1, p2);

        when(pedidoRepository.findAll()).thenReturn(listaPedidos);

        List<PedidoResponse> resultado = pedidoService.getAllPedidos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

		assertEquals(1L, resultado.get(0).id());
        assertEquals("Pedido 1", resultado.get(0).numeroPedido());
        assertEquals(new BigDecimal(10), resultado.get(0).preco());

		assertEquals(2L, resultado.get(1).id());
        assertEquals("Pedido 2", resultado.get(1).numeroPedido());
		assertEquals(new BigDecimal(20), resultado.get(1).preco());

		verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        Long id = 1L;
        when(pedidoRepository.findById(id)).thenReturn(Optional.of(pedido));

        PedidoResponse pedidoExiste = pedidoService.findById(id);

        assertNotNull(pedidoExiste);
        assertEquals(1L, pedidoExiste.id());
        assertEquals("a12bc3", pedidoExiste.numeroPedido());
        assertEquals("SKU123", pedidoExiste.skucode());
        assertEquals(new BigDecimal(140), pedidoExiste.preco());
        assertEquals(2, pedidoExiste.quantidade());
        verify(pedidoRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_Fail(){
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        PedidoNotFoundException exception = assertThrows(PedidoNotFoundException.class, 
            () -> pedidoService.findById(idInexistente));
        assertEquals("Pedido não encontrado.", exception.getMessage());    
        verify(pedidoRepository, times(1)).findById(idInexistente);
    }

    @Test
    void testDeletePedido_Success(){
        when(pedidoRepository.findById(pedido.getId())).thenReturn(Optional.of(pedido));

        pedidoService.deletePedido(pedido.getId());
        verify(pedidoRepository, times(1)).findById(pedido.getId());
        verify(pedidoRepository, times(1)).delete(pedido);
    }

    @Test
    void testDeletePedido_Fail(){
        Long idInexistente = 999L;

        when(pedidoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException exception =assertThrows(RuntimeException.class, 
            () -> pedidoService.deletePedido(idInexistente));
        assertEquals("Pedido não encontrado.", exception.getMessage());
        verify(pedidoRepository, never()).delete(any());
    }
}
