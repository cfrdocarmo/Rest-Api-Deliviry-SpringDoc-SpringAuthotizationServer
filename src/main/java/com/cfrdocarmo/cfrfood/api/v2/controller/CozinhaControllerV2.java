package com.cfrdocarmo.cfrfood.api.v2.controller;

import com.cfrdocarmo.cfrfood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.cfrdocarmo.cfrfood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.cfrdocarmo.cfrfood.api.v2.model.CozinhaModelV2;
import com.cfrdocarmo.cfrfood.api.v2.model.input.CozinhaInputV2;
import com.cfrdocarmo.cfrfood.api.v2.openapi.controller.CozinhaControllerV2OpenApi;
import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import com.cfrdocarmo.cfrfood.domain.repository.CozinhaRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v2/cozinhas")
public class CozinhaControllerV2 {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CozinhaModelAssemblerV2 cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassemblerV2 cozinhaInputDisassembler;

	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
	@GetMapping()
	public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable) {

		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

		PagedModel<CozinhaModelV2> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage,cozinhaModelAssembler);

		return cozinhasPagedModel;
	}
	
	@GetMapping(value = "/{cozinhaId}" )
	public CozinhaModelV2 buscarPorId(@PathVariable Long cozinhaId) {
		return cozinhaModelAssembler.toModel(cadastroCozinha.buscarOuFalhar(cozinhaId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModelV2 adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInput) {

		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);

		return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));
	}

	
	@PutMapping(value = "/{cozinhaId}")
	public CozinhaModelV2 atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInputV2 cozinhaInput){

		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);

		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);

		return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual));
	}
	
	@DeleteMapping(value = "/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId){
			cadastroCozinha.excluir(cozinhaId);
	}
	
}
