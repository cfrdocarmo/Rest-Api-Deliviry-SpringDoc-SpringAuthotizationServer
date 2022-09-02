package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.assembler.GrupoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.GrupoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.PermissaoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.PermissaoModel;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.GrupoPermissaoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Grupo;
import com.cfrdocarmo.cfrfood.domain.service.CadastroGrupoService;
import com.cfrdocarmo.cfrfood.domain.service.CadastroPermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/grupos/{grupoId}/permissoes")
public class GrupoPermissaoController implements GrupoPermissaoControllerOpenApi {

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Autowired
    private CadastroPermissaoService cadastroPermissao;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;

    @Autowired
    private PermissaoModelAssembler permissaoModelAssembler;

    @Autowired
    private CFRdoCarmoLinks links;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<PermissaoModel> listar (@PathVariable Long grupoId) {
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        CollectionModel<PermissaoModel> permissoesModel = permissaoModelAssembler.toCollectionModel(grupo.getPermissoes())
                .removeLinks()
                .add(links.linkToGrupoPermissoes(grupoId))
                .add(links.linkToGrupoPermissaoAssociacao(grupoId, "associar"));

        permissoesModel.getContent().forEach(permissaoModel -> {
            permissaoModel.add(links.linkToGrupoPermissaoDesassociacao(grupoId, "desassociar"));
        });

        return permissoesModel;
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarPermissao(@PathVariable Long grupoId , @PathVariable Long permissaoId) {
        cadastroGrupo.associarPermissao(grupoId, permissaoId);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{permissaoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerPermissao(@PathVariable Long grupoId , @PathVariable Long permissaoId) {
        cadastroGrupo.desassociarPermissao(grupoId, permissaoId);

        return ResponseEntity.noContent().build();
    }

}
