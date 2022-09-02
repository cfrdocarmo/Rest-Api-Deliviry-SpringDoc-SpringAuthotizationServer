package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.GrupoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.GrupoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.GrupoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.GrupoInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.GrupoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Grupo;
import com.cfrdocarmo.cfrfood.domain.repository.GrupoRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroGrupoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path ="/v1/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
public class GrupoController implements GrupoControllerOpenApi {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        List<Grupo> grupos = grupoRepository.findAll();

        return grupoModelAssembler.toCollectionModel(grupos);
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{grupoId}")
    public GrupoModel buscarPorId(@PathVariable Long grupoId) {
        return grupoModelAssembler.toModel(cadastroGrupo.buscarOuFalhar(grupoId));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoModel adicionar(@RequestBody @Valid GrupoInput grupoInput) {
        Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

        return grupoModelAssembler.toModel(cadastroGrupo.salvar(grupo));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{grupoId}")
    public GrupoModel atualizar(@PathVariable Long grupoId, @RequestBody @Valid GrupoInput grupoInput) {

        Grupo grupoAtual = cadastroGrupo.buscarOuFalhar(grupoId);

        grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);

        return grupoModelAssembler.toModel(cadastroGrupo.salvar(grupoAtual));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long grupoId) {
        cadastroGrupo.excluir(grupoId);
    }

}
