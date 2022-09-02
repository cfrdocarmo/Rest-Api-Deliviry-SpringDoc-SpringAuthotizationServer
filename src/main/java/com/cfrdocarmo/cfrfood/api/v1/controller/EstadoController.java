package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.EstadoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.EstadoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.EstadoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.EstadoInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.EstadoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Estado;
import com.cfrdocarmo.cfrfood.domain.repository.EstadoRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/estados")
public class EstadoController implements EstadoControllerOpenApi {

	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CadastroEstadoService cadastroEstado;

	@Autowired
	private EstadoModelAssembler estadoModelAssembler;

	@Autowired
	private EstadoInputDisassembler estadoInputDisassembler;

	@CheckSecurity.Estados.PodeConsultar
	@GetMapping
	public CollectionModel<EstadoModel> listar() {
		return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
	}

	@CheckSecurity.Estados.PodeConsultar
	@GetMapping("/{estadoId}")
	public EstadoModel buscarPorId(@PathVariable Long estadoId){

		Estado estado = cadastroEstado.buscarOuFalhar(estadoId);

		return estadoModelAssembler.toModel(estado);
	}

	@CheckSecurity.Estados.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput){

		Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);

		return estadoModelAssembler.toModel(cadastroEstado.salvar(estado));
	}

	@CheckSecurity.Estados.PodeConsultar
	@PutMapping("/{estadoId}")
	public EstadoModel atualizar(@PathVariable Long estadoId, @RequestBody @Valid EstadoInput estadoInput){


		Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);

		estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

		return estadoModelAssembler.toModel(cadastroEstado.salvar(estadoAtual));
	}

	@CheckSecurity.Estados.PodeConsultar
	@DeleteMapping("/{estadoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long estadoId) {
		cadastroEstado.excluir(estadoId);
	}
	
}
