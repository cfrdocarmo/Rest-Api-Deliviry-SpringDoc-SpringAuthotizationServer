package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.ResourceUriHelper;
import com.cfrdocarmo.cfrfood.api.v1.assembler.CidadeInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.CidadeModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.CidadeModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.CidadeInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.exception.EstadoNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.exception.NegocioException;
import com.cfrdocarmo.cfrfood.domain.model.Cidade;
import com.cfrdocarmo.cfrfood.domain.repository.CidadeRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Autowired
	private CidadeModelAssembler cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassembler cidadeInputDisassembler;

	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping()
	public CollectionModel<CidadeModel> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();

		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping(path = "/{cidadeId}")
	public CidadeModel buscarPorId(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

		return cidadeModelAssembler.toModel(cidade);
	}

	@CheckSecurity.Cidades.PodeEditar
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

			cidade = cadastroCidade.salvar(cidade);

			CidadeModel cidadeModel = cidadeModelAssembler.toModel(cidade);

			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getId());

			return cidadeModel;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@CheckSecurity.Cidades.PodeEditar
	@PutMapping(path = "/{cidadeId}")
	public CidadeModel atualizar(@PathVariable Long cidadeId,
								 @RequestBody @Valid CidadeInput cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

			cidadeAtual = cadastroCidade.salvar(cidadeAtual);

			return cidadeModelAssembler.toModel(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@CheckSecurity.Cidades.PodeEditar
	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}

}