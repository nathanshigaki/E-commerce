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
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.produto_service.dto.ProdutoDto;
import com.projeto.produto_service.dto.ProdutoUpdateDto;
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
	private ProdutoDto produtoDto;
	private ProdutoUpdateDto updateDto;

	@BeforeEach
	void setUp() {
		produto = Produto.builder()
				.id("a12bc3")
				.nome("Garrafa")
				.descricao("Garrafa cinza de 500ml")
				.preco(new BigDecimal("90"))
				.build();
		
		produtoDto = new ProdutoDto(
			produto.getId(),
			produto.getNome(),
			produto.getDescricao(),
			produto.getPreco()
		);

		updateDto = new ProdutoUpdateDto();
	}

	@Test
	void testCreateProduto_Success(){
		when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

		Produto resultado = produtoService.createProduto(produtoDto);

		assertEquals("a12bc3", resultado.getId());
		assertEquals("Garrafa", resultado.getNome());
		assertEquals("Garrafa cinza de 500ml", resultado.getDescricao());
		assertEquals(new BigDecimal("90"), resultado.getPreco());
		verify(produtoRepository, times(1)).save(any(Produto.class));
	}

	@Test
	void testCreateProduto_Fail(){
		ProdutoDto produtoInvalido = new ProdutoDto(
			"id_teste",
			"Nome Teste",
			"Descricao Teste",
			new BigDecimal("-50.00")
    	);

		assertThrows(IllegalArgumentException.class, () -> produtoService.createProduto(produtoInvalido));
		verify(produtoRepository, never()).save(any());
	}

	@Test
	void testFindById_Success(){
		when(produtoRepository.findById(produtoDto.id())).thenReturn(Optional.of(produto));

		Produto produtoExistente = produtoService.findById(produtoDto.id());

		assertNotNull(produtoExistente);
		assertEquals("a12bc3", produtoExistente.getId());
		assertEquals("Garrafa", produtoExistente.getNome());
		assertEquals(new BigDecimal(90), produtoExistente.getPreco());
		verify(produtoRepository, times(1)).findById(produtoDto.id());
	}

	@Test
	void testFindById_Fail(){
		String idInexistente = "asdasdasdsadsds";
		when(produtoRepository.findById(idInexistente)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, 
			() -> produtoService.findById(idInexistente));
		assertEquals("Produto não encontrado.", exception.getMessage());
    	verify(produtoRepository, times(1)).findById(idInexistente);
	}

	@Test
	void testUpdateProduto_Success(){
		updateDto.setPreco(new BigDecimal(200));

		when(produtoRepository.findById(produtoDto.id())).thenReturn(Optional.of(produto));
		when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Produto resultado = produtoService.updateProduto(produtoDto.id(), updateDto);

		assertEquals(new BigDecimal(200), resultado.getPreco());
		assertEquals("Garrafa", resultado.getNome());
		assertEquals("Garrafa cinza de 500ml", resultado.getDescricao());
		verify(produtoRepository, times(1)).save(any(Produto.class));
	}

	@Test
	void testUpdateProduto_Fail(){
		updateDto.setPreco(new BigDecimal(-50));

		when(produtoRepository.findById(produtoDto.id())).thenReturn(Optional.of(produto));

		RuntimeException exception = assertThrows(RuntimeException.class, 
			() -> produtoService.updateProduto(produtoDto.id(), updateDto));
		assertEquals("O preço não pode ser negativo.", exception.getMessage());
		assertEquals(new BigDecimal(90), produto.getPreco());
		verify(produtoRepository, times(0)).save(any(Produto.class));
	}

	@Test
	void testDeleteProduto_Success(){
		when(produtoRepository.findById(produtoDto.id())).thenReturn(Optional.of(produto));

		produtoService.deleteProduto(produtoDto.id());
		verify(produtoRepository, times(1)).findById(produtoDto.id());
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
