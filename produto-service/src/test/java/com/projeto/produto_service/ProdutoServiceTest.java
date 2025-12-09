package com.projeto.produto_service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
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

import com.projeto.produto_service.dto.ProdutoRequest;
import com.projeto.produto_service.dto.ProdutoResponse;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
import com.projeto.produto_service.exception.ProdutoNotFoundException;
import com.projeto.produto_service.model.Produto;
import com.projeto.produto_service.repository.ProdutoRepository;
import com.projeto.produto_service.service.ProdutoService;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

	@InjectMocks
	private ProdutoService produtoService;

	@Mock
	private ProdutoRepository produtoRepository;

	private Produto produto;
	private ProdutoRequest produtoRequest;
	private ProdutoUpdateDto updateDto;

	@BeforeEach
	void setUp() {
		produto = Produto.builder()
				.id("a12bc3")
				.nome("Garrafa")
				.descricao("Garrafa cinza de 500ml")
				.preco(new BigDecimal("90"))
				.build();
		
		produtoRequest = new ProdutoRequest(
			produto.getNome(),
			produto.getDescricao(),
			produto.getPreco()
		);

		updateDto = new ProdutoUpdateDto();
	}

	@Test
	void testCreateProduto_Success(){
		when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

		ProdutoResponse resultado = produtoService.createProduto(produtoRequest);

		assertEquals("a12bc3", resultado.id());
		assertEquals("Garrafa", resultado.nome());
		assertEquals("Garrafa cinza de 500ml", resultado.descricao());
		assertEquals(new BigDecimal("90"), resultado.preco());
		verify(produtoRepository, times(1)).save(any(Produto.class));
	}
 
	@Test
	void testCreateProduto_Fail(){
		ProdutoRequest produtoInvalido = new ProdutoRequest(
			"Nome Teste",
			"Descricao Teste",
			new BigDecimal("-50.00")
    	);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
			() -> produtoService.createProduto(produtoInvalido));
		assertEquals("O preço deve ser maior que zero.", exception.getMessage());
		verify(produtoRepository, never()).save(any());
	}

	@Test
	void testGetAllProduto(){
		Produto p1 = Produto.builder()
                .id("1")
                .nome("Produto A")
                .descricao("Descrição A")
                .preco(new BigDecimal("10.00"))
                .build();

        Produto p2 = Produto.builder()
                .id("2")
                .nome("Produto B")
                .descricao("Descrição B")
                .preco(new BigDecimal("20.00"))
                .build();

		List<Produto> listaProdutos = List.of(p1, p2);

		when(produtoRepository.findAll()).thenReturn(listaProdutos);

		List<ProdutoResponse> resultado = produtoService.getAllProdutos();

		assertNotNull(resultado);
        assertEquals(2, resultado.size());

		assertEquals("1", resultado.get(0).id());
        assertEquals("Produto A", resultado.get(0).nome());
        assertEquals(new BigDecimal("10.00"), resultado.get(0).preco());

		assertEquals("2", resultado.get(1).id());
        assertEquals("Produto B", resultado.get(1).nome());
		assertEquals(new BigDecimal("20.00"), resultado.get(1).preco());

		verify(produtoRepository, times(1)).findAll();
	}

	@Test
	void testFindById_Success(){
		String id = "a12bc3";
		when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

		ProdutoResponse produtoExistente = produtoService.findById(id);

		assertNotNull(produtoExistente);
		assertEquals("a12bc3", produtoExistente.id());
		assertEquals("Garrafa", produtoExistente.nome());
		assertEquals(new BigDecimal(90), produtoExistente.preco());
		verify(produtoRepository, times(1)).findById(id);
	}

	@Test
	void testFindById_Fail(){
		String idInexistente = "asdasdasdsadsds";
		when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

		ProdutoNotFoundException exception = assertThrows(ProdutoNotFoundException.class, 
			() -> produtoService.findById(idInexistente));
		assertEquals("Produto não encontrado.", exception.getMessage());
    	verify(produtoRepository, times(1)).findById(idInexistente);
	}

	@Test
	void testUpdateProduto_Success(){
		String id = "a12bc3";
		updateDto.setPreco(new BigDecimal(200));

		when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ProdutoResponse resultado = produtoService.updateProduto(id, updateDto);

		assertEquals(new BigDecimal(200), resultado.preco());
		assertEquals("Garrafa", resultado.nome());
		assertEquals("Garrafa cinza de 500ml", resultado.descricao());
		verify(produtoRepository, times(1)).save(any(Produto.class));
	}

	@Test
	void testUpdateProduto_Fail(){
		String id = "a12bc3";
		updateDto.setPreco(new BigDecimal(-50));

		when(produtoRepository.findById(id)).thenReturn(Optional.of(produto));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
			() -> produtoService.updateProduto(id, updateDto));
		assertEquals("O preço deve ser maior que zero.", exception.getMessage());
		assertEquals(new BigDecimal(90), produto.getPreco());
		verify(produtoRepository, times(0)).save(any(Produto.class));
	}

	@Test
	void testDeleteProduto_Success(){
		when(produtoRepository.findById(produto.getId())).thenReturn(Optional.of(produto));

		produtoService.deleteProduto(produto.getId());
		verify(produtoRepository, times(1)).findById(produto.getId());
		verify(produtoRepository, times(1)).delete(produto);
	}

	@Test
	void testDeleteProduto_Fail(){
		String id = "asdasdasd";

		when(produtoRepository.findById(id)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, 
			() -> produtoService.deleteProduto(id)
		);
		assertEquals("Produto não encontrado.", exception.getMessage());
		verify(produtoRepository, never()).delete(any());
	}

}
