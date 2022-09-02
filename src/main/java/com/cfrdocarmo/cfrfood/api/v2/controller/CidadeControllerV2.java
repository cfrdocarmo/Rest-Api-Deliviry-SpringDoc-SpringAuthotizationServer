package com.cfrdocarmo.cfrfood.api.v2.controller;

import com.cfrdocarmo.cfrfood.api.ResourceUriHelper;
import com.cfrdocarmo.cfrfood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.cfrdocarmo.cfrfood.api.v2.assembler.CidadeModelAssemblerV2;
import com.cfrdocarmo.cfrfood.api.v2.model.CidadeModelV2;
import com.cfrdocarmo.cfrfood.api.v2.model.input.CidadeInputV2;
import com.cfrdocarmo.cfrfood.api.v2.openapi.controller.CidadeControllerV2OpenApi;
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
@RequestMapping(path = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {

	@Autowired
	private CidadeRepository cidadeRepository;

	@Autowired
	private CadastroCidadeService cadastroCidade;

	@Autowired
	private CidadeModelAssemblerV2 cidadeModelAssembler;

	@Autowired
	private CidadeInputDisassemblerV2 cidadeInputDisassembler;

	@GetMapping()
	public CollectionModel<CidadeModelV2> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();

		return cidadeModelAssembler.toCollectionModel(todasCidades);
	}

	@GetMapping(path = "/{cidadeId}")
	public CidadeModelV2 buscarPorId(@PathVariable Long cidadeId) {
		Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

		return cidadeModelAssembler.toModel(cidade);
	}

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeModelV2 adicionar(@RequestBody @Valid CidadeInputV2 cidadeInput) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

			cidade = cadastroCidade.salvar(cidade);

			CidadeModelV2 cidadeModel = cidadeModelAssembler.toModel(cidade);

			ResourceUriHelper.addUriInResponseHeader(cidadeModel.getIdCidade());

			return cidadeModel;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping(path = "/{cidadeId}")
	public CidadeModelV2 atualizar(@PathVariable Long cidadeId,
								 @RequestBody @Valid CidadeInputV2 cidadeInput) {
		try {
			Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

			cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

			cidadeAtual = cadastroCidade.salvar(cidadeAtual);

			return cidadeModelAssembler.toModel(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cadastroCidade.excluir(cidadeId);
	}

}