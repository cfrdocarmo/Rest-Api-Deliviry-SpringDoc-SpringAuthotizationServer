package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.EstadoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.EstadoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Estados")
public interface EstadoControllerOpenApi {

    @Operation(summary = "Lista os estados")
    public CollectionModel<EstadoModel> listar();

    @Operation(summary = "Busca um estado por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do estado inválido.",
            content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado.",
            content = @Content(schema = @Schema(ref = "Problema")))
    })
    public EstadoModel buscarPorId(@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId);

    @Operation(summary = "Cadastra um estado", description = "Cadastro de  um estado necessita de um nome válido.", responses = {
            @ApiResponse(responseCode = "201", description = "Cadastro realizado com sucesso.")
    })
    public EstadoModel adicionar(@RequestBody(description = "Representação de um novo estado.") EstadoInput estadoInput);

    @Operation(summary = "Atualiza um estado por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do estado inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado.",
                    content = @Content(schema = @Schema(ref = "Problema")))
    })
    public EstadoModel atualizar(@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId,
                                 @RequestBody(description = "Representação de um estado com dados atualizados.") EstadoInput estadoInput);

    @Operation(summary = "Exclui um estado por Id", responses = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "ID do estado inválido.",
                    content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Estado não encontrado.",
                    content = @Content(schema = @Schema(ref = "Problema")))
    })
    public void remover(@Parameter(description = "ID de um estado", example = "1", required = true) Long estadoId);

}
