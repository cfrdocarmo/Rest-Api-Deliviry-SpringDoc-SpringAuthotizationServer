package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.FotoProdutoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.FotoProdutoInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Produtos")
public interface RestauranteFotoProdutoControllerOpenApi {

    @Operation(summary = "Atualiza a foto de um produto de um restaurante")
    public FotoProdutoModel atualizarFoto(@Parameter(description = "Id do Restaurante", example = "1") Long restauranteId,
                                          @Parameter(description = "Id do Produto", example = "1")Long produtoId,
                                          @RequestBody(required = true) FotoProdutoInput fotoProdutoInput) throws IOException;


    @Operation(summary = "Exclui a foto de do produto de um restaurante", responses = {
            @ApiResponse(responseCode = "204", description = "Foto do produto excluída.",
            content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "400", description = "ID do restaurante ou do produto inválido",
            content = @Content(schema = @Schema(ref = "Problema"))),
            @ApiResponse(responseCode = "404", description = "Foto do produto náo encontrada.",
                    content = @Content(schema = @Schema(ref = "Problema"))),
    })
    ResponseEntity<Void> excluir(Long restauranteId,Long produtoId);


    @Operation(summary = "Busca foto do produto de um restaurante", responses = {
            @ApiResponse(responseCode = "200", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = FotoProdutoModel.class)),
                    @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary")),
                    @Content(mediaType = "image/png", schema = @Schema(type = "string", format = "binary"))
            }),
            @ApiResponse(responseCode = "400", description = "ID do restaurante ou produto inválido", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
            @ApiResponse(responseCode = "404", description = "Foto de produto não encontrada", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),

    })
    FotoProdutoModel buscar(
            Long restauranteId,Long produtoId)
            throws HttpMediaTypeNotAcceptableException;


    @Operation(hidden = true)
    public ResponseEntity<?> servir(Long restauranteId, Long produtoId, String acceptHeader)
            throws HttpMediaTypeNotAcceptableException;


}