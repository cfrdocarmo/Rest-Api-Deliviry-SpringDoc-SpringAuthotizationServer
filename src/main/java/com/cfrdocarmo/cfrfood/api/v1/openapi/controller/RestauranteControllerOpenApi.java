package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteApenasNomeModel;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteBasicoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.RestauranteInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Restaurante")
public interface RestauranteControllerOpenApi {

    @Operation(parameters = {
        @Parameter(
                name = "projecao",
                description = "Nome da projeção",
                example = "apenas-nome",
                in = ParameterIn.QUERY,
                required = false
        )
    })
    public CollectionModel<RestauranteBasicoModel> listar();

    @Operation(hidden = true)
    public CollectionModel<RestauranteApenasNomeModel> listarApenasNomes();

    @Operation(summary = "Busca um restaurante por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do restaurante inválido",
            content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
            content = @Content(schema = @Schema(ref = "Problema")))
    })
    public RestauranteModel buscar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Cadastra um restaurante", responses = {
            @ApiResponse(responseCode = "200",description = "Cadastro de um restaurante necessita de um cozinha válida."),
    })
    public RestauranteModel adicionar(@RequestBody(description = "Representação de um restaurante", required = true) RestauranteInput restauranteInput);

    @Operation(summary = "Busca um restaurante por Id", responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "ID do restaurante inválido",
                    content = @Content(schema = @Schema(ref = "Problema"))
            ),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado",
                    content = @Content(schema = @Schema(ref = "Problema")))
    })
    public RestauranteModel atualizar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId,
                                      @RequestBody(description = "Representação de um restaurante com os novos dados", required = true) RestauranteInput restauranteInput);

    @Operation(summary = "Ativa um restaurante por Id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> ativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Inativa um restaurante po Id", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> inativar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Ativa múltiplos restaurantes", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes ativados com sucesso"),
    })
    public void ativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restaurantesIds);

    @Operation(summary = "Inativa múltiplos restaurantes", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurantes inativados com sucesso"),
    })
    public void inativarMultiplos(@RequestBody(description = "IDs de restaurantes", required = true) List<Long> restaurantesIds);

    @Operation(summary = "Abre um restaurante por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante aberto com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    ResponseEntity<Void> abrir(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

    @Operation(summary = "Fecha um restaurante por ID", responses = {
            @ApiResponse(responseCode = "204", description = "Restaurante fechado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    ResponseEntity<Void> fechar(@Parameter(description = "ID de um restaurante", example = "1", required = true) Long restauranteId);

}
