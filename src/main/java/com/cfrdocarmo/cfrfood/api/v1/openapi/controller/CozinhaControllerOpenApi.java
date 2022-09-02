package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.CozinhaModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.CozinhaInput;
import com.cfrdocarmo.cfrfood.core.springdoc.PageableParameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Cozinhas")
public interface CozinhaControllerOpenApi {

    @PageableParameter
    @Operation(summary = "Lista as Cozinhas")
    public PagedModel<CozinhaModel> listar(@Parameter(hidden = true) Pageable pageable);

    @Operation(summary = "Busca uma cozinha por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Id da cozinha inválido.",
            content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
            content = @Content(schema = @Schema(ref = "Problema"))
            )
    })
    public CozinhaModel buscarPorId(Long cozinhaId);

    @Operation(summary = "Cadastra uma cozinha", description = "Cadastro de uma cozinha necessita de um nome válido.")
    public CozinhaModel adicionar(CozinhaInput cozinhaInput);

    @Operation(summary = "Atualiza uma cozinha", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Id da cozinha inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
                    content = @Content(schema = @Schema(ref = "Problema"))
            )
    })
    public CozinhaModel atualizar(Long cozinhaId, CozinhaInput cozinhaInput);

    @Operation(summary = "Excluí uma cozinha por Id", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "Id da cozinha inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Cozinha não encontrada",
                    content = @Content(schema = @Schema(ref = "Problema"))
            )
    })
    public void remover(Long cozinhaId);


}
