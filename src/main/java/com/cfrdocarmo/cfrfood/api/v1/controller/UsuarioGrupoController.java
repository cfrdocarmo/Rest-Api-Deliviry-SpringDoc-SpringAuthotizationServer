package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.assembler.GrupoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.UsuarioInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.UsuarioModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.GrupoModel;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.UsuarioGrupoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Usuario;
import com.cfrdocarmo.cfrfood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1//usuarios/{usuarioId}/grupos")
public class UsuarioGrupoController implements UsuarioGrupoControllerOpenApi {

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;

    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @Autowired
    private CFRdoCarmoLinks links;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        CollectionModel<GrupoModel> grupoModels = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
                .removeLinks()
                .add(links.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

        grupoModels.getContent().forEach(grupoModel -> {
            grupoModel.add(links.linkToUsuarioGrupoDesassociacao(usuarioId, "desassociar"));
        });

        return grupoModels;
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adicionarGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.adicionarGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> removerGrupo(@PathVariable Long usuarioId, @PathVariable Long grupoId) {
        cadastroUsuario.removerGrupo(usuarioId, grupoId);

        return ResponseEntity.noContent().build();
    }

}
