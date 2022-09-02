package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.CozinhaInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.CozinhaModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.CozinhaModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.CozinhaInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.CozinhaControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import com.cfrdocarmo.cfrfood.domain.repository.CozinhaRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroCozinhaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/v1/cozinhas")
public class CozinhaController implements CozinhaControllerOpenApi {

	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	@Autowired
	private CadastroCozinhaService cadastroCozinha;

	@Autowired
	private CozinhaModelAssembler cozinhaModelAssembler;

	@Autowired
	private CozinhaInputDisassembler cozinhaInputDisassembler;

	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

//	@PreAuthorize("isAuthenticated()")
//	@PodeConsultarCozinhas
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping()
	public PagedModel<CozinhaModel> listar(@PageableDefault(size = 10) Pageable pageable) {

		log.info("Consultando cozinhas com páginas de {} registros...", pageable.getPageSize());

//		if( true ) {
//			throw new RuntimeException("Teste de Exception");
//		}

		Page<Cozinha> cozinhasPage = cozinhaRepository.findAll(pageable);

		PagedModel<CozinhaModel> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage,cozinhaModelAssembler);

		return cozinhasPagedModel;
	}

//	@PreAuthorize("isAuthenticated()")
//	@PodeConsultarCozinhas
	@CheckSecurity.Cozinhas.PodeConsultar
	@GetMapping(value = "/{cozinhaId}" )
	public CozinhaModel buscarPorId(@PathVariable Long cozinhaId) {
		return cozinhaModelAssembler.toModel(cadastroCozinha.buscarOuFalhar(cozinhaId));
	}

//	@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
//	@PodeEditarCozinhas
	@CheckSecurity.Cozinhas.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {

		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);

		return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinha));
	}

//	@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
//	@PodeEditarCozinhas
	@CheckSecurity.Cozinhas.PodeEditar
	@PutMapping(value = "/{cozinhaId}")
	public CozinhaModel atualizar(@PathVariable Long cozinhaId, @RequestBody @Valid CozinhaInput cozinhaInput){

		Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);

		cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);

		return cozinhaModelAssembler.toModel(cadastroCozinha.salvar(cozinhaAtual));
	}

	//@PreAuthorize("hasAuthority('EDITAR_COZINHAS')")
//	@PodeEditarCozinhas
	@CheckSecurity.Cozinhas.PodeEditar
	@DeleteMapping(value = "/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId){
			cadastroCozinha.excluir(cozinhaId);
	}
	
}
