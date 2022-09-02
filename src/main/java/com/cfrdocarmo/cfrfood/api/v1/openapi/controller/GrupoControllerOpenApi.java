package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.GrupoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.GrupoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;
import io.swagger.v3.oas.models.media.*;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Grupos")
public interface GrupoControllerOpenApi {

    @Operation(summary = "Lista de grupos")
    public CollectionModel<GrupoModel> listar();

    @Operation(summary = "Busca um grupo por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido.",
            content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado",
            content = @Content(schema = @Schema(ref = "Problema")))
    })
    public GrupoModel buscarPorId(Long grupoId);

    @Operation(summary = "Cadastra um novo grupo")
    public GrupoModel adicionar(GrupoInput grupoInput);

    @Operation(summary = "Atualiza um grupo por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado",
                    content = @Content(schema = @Schema(ref = "Problema")))
    })
    public GrupoModel atualizar(Long grupoId,
                                GrupoInput grupoInput);

    @Operation(summary = "Exclui um grupo por Id", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "ID do grupo inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Grupo não encontrado",
                    content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void remover(Long grupoId);

}
