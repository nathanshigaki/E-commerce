package com.projeto.inventario_service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.inventario_service.Repository.InventarioRepository;
import com.projeto.inventario_service.Service.InventarioService;
import com.projeto.inventario_service.dto.InventarioRequest;
import com.projeto.inventario_service.dto.InventarioResponse;
import com.projeto.inventario_service.model.Inventario;

@ExtendWith(MockitoExtension.class)
class InventarioServiceApplicationTests {

	@InjectMocks
	private InventarioService inventarioService;

	@Mock
	private InventarioRepository inventarioRepository;

	private Inventario inventario;

	@BeforeEach
	void setUp() {
		inventario = new Inventario();
		inventario.setSkucode("Mouse_Logitech");
		inventario.setQuantidade(5);
	}

	@Test
	void isInStock_Success(){
		when(inventarioRepository.existsBySkucodeAndQuantidadeIsGreaterThanEqual(inventario.getSkucode(), inventario.getQuantidade())).thenReturn(true);
		Boolean resultado = inventarioService.isInStock(inventario.getSkucode(), inventario.getQuantidade());

		assertEquals(true, resultado);
	}

	@Test
	void isInStock_Fail(){
		when(inventarioRepository.existsBySkucodeAndQuantidadeIsGreaterThanEqual(inventario.getSkucode(), inventario.getQuantidade())).thenReturn(false);
		Boolean resultado = inventarioService.isInStock(inventario.getSkucode(), inventario.getQuantidade());

		assertEquals(false, resultado);
	}

	@Test
	void getInventarioBySkucode_Success() {
		when(inventarioRepository.findAllBySkucode("Mouse_Logitech")).thenReturn(List.of(inventario));

		InventarioResponse response = inventarioService.getInventarioBySkucode("Mouse_Logitech");
		
		assertNotNull(response);
		assertEquals("Mouse_Logitech", response.skucode());
		verify(inventarioRepository, times(1)).findAllBySkucode("Mouse_Logitech");
	}

	@Test
	void getInventarioBySkucode_NotFound() {
		when(inventarioRepository.findAllBySkucode("vazio")).thenReturn(Collections.emptyList());

		assertThrows(RuntimeException.class, () -> {
			inventarioService.getInventarioBySkucode("vazio");
		});
		
		verify(inventarioRepository, times(1)).findAllBySkucode("vazio");
	}

	@Test
    void updateInventario_Success() {
        InventarioRequest request = new InventarioRequest("Mouse_Logitech", 20);
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        InventarioResponse response = inventarioService.updateInventario(1L, request);

        assertNotNull(response);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }
}
