package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.RestauranteApenasNomeModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.RestauranteInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.RestauranteModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteApenasNomeModel;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteBasicoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.RestauranteInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.RestauranteControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.exception.CidadeNaoEncontradaException;
import com.cfrdocarmo.cfrfood.domain.exception.CozinhaNaoEncontradaException;
import com.cfrdocarmo.cfrfood.domain.exception.NegocioException;
import com.cfrdocarmo.cfrfood.domain.exception.RestauranteNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.Restaurante;
import com.cfrdocarmo.cfrfood.domain.repository.RestauranteRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/restaurantes")
public class RestauranteController implements RestauranteControllerOpenApi {

	@Autowired
	private RestauranteRepository restauranteRepository;
	
	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@Autowired
	private RestauranteModelAssembler restauranteModelAssembler;

	@Autowired
	private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

	@Autowired
	private RestauranteApenasNomeModelAssembler restauranteApenasNomeModelAssembler;

	@Autowired
	private RestauranteInputDisassembler restauranteInputDisassembler;

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<RestauranteBasicoModel> listar() {
		return restauranteBasicoModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping(params = "projecao=apenas-nome")
	public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes() {
		return restauranteApenasNomeModelAssembler.toCollectionModel(restauranteRepository.findAll());
	}

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{restauranteId}")
	public RestauranteModel buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		return restauranteModelAssembler.toModel(restaurante);
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteModel adicionar(@RequestBody @Valid RestauranteInput restauranteInput) {
		try{
			Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}")
	public RestauranteModel atualizar(@PathVariable Long restauranteId, @RequestBody @Valid RestauranteInput restauranteInput){

		try {
			Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

			restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);

			return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
		} catch (CozinhaNaoEncontradaException | CidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> ativar(@PathVariable Long restauranteId) {

		cadastroRestaurante.ativar(restauranteId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/fechamento")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> fechar(@PathVariable Long restauranteId) {

		cadastroRestaurante.fechar(restauranteId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{restauranteId}/abertura")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> abrir(@PathVariable Long restauranteId) {

		cadastroRestaurante.abrir(restauranteId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> inativar(@PathVariable Long restauranteId) {

		cadastroRestaurante.inativar(restauranteId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@PutMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.ativarMultiplos(restaurantesIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/ativacoes")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativarMultiplos(@RequestBody List<Long> restaurantesIds) {
		try {
			cadastroRestaurante.inativarMultiplos(restaurantesIds);
		} catch (RestauranteNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@CheckSecurity.Restaurantes.PodeGerenciarCadastro
	@DeleteMapping("/{restauranteId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long restauranteId){
		cadastroRestaurante.excluir(restauranteId);
	}

}
