package com.projeto.inventario_service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.inventario_service.Repository.InventarioRepository;
import com.projeto.inventario_service.Service.InventarioService;
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

}
