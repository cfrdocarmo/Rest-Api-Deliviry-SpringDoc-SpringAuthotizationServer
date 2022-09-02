package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.PermissaoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.PermissaoModel;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.PermissaoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Permissao;
import com.cfrdocarmo.cfrfood.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/permissoes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PermissaoController implements PermissaoControllerOpenApi {

	@Autowired
	private PermissaoRepository permissaoRepository;

	@Autowired
	private PermissaoModelAssembler permissaoModelAssembler;

	@CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
	@GetMapping
	public CollectionModel<PermissaoModel> listar() {
		List<Permissao> todasAsPermissoes = permissaoRepository.findAll();

		CollectionModel<PermissaoModel> permissaoModels = permissaoModelAssembler.toCollectionModel(todasAsPermissoes);

		return permissaoModels;
	}

}
