package com.cfrdocarmo.cfrfood;

import com.cfrdocarmo.cfrfood.domain.exception.EntidadeEmUsoException;
import com.cfrdocarmo.cfrfood.domain.exception.EntidadeNaoEncontradaException;
import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import com.cfrdocarmo.cfrfood.domain.service.CadastroCozinhaService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCozinhaITOld {

	@LocalServerPort
	private int port;

	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Test
	public void deveAtribuirId_QuandoCadastrarCozinhaComDadosCorretos() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");
		novaCozinha = cadastroCozinha.salvar(novaCozinha);

		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}

	@Test()
	public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);

		ConstraintViolationException erroEsperado = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			cadastroCozinha.salvar(novaCozinha);
		});

		assertThat(erroEsperado).isNotNull();
	}

	@Test
	public void deveFalhar_QuandoExluirCozinhaEmUso() {
		EntidadeEmUsoException erroEsperado = Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
			cadastroCozinha.excluir(1L);
		});

		assertThat(erroEsperado).isInstanceOf(EntidadeEmUsoException.class);
	}

	@Test
	public void deveFalhar_QuandoExluirCozinhaInexistente() {
		EntidadeNaoEncontradaException erroEsperado = Assertions.assertThrows(EntidadeNaoEncontradaException.class, () -> {
			cadastroCozinha.excluir(100L);
		});

		assertThat(erroEsperado).isInstanceOf(EntidadeNaoEncontradaException.class);
	}

	//===========================================================================================================================



}
