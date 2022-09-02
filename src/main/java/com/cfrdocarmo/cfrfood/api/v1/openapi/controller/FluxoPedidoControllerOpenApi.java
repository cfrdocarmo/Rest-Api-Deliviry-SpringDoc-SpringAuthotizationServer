package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pedidos")
public interface FluxoPedidoControllerOpenApi {

    @Operation(summary = "Confirmação de pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido Confirmado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado.",
            content = @Content(schema = @Schema(ref = "Problema")))

    })
    public ResponseEntity<Void> confirmar(String codigoPedido);

    @Operation(summary = "Registrar entrega de pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Entrega de pedido registrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
            content = @Content(schema = @Schema(ref = "Problema")))
    })
    public ResponseEntity<Void> entregar(String codigoPedido);

    @Operation(summary = "Cancelamento de pedido", responses = {
            @ApiResponse(responseCode = "204", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = {
                    @Content(schema = @Schema(ref = "Problema")) }),
    })
    public ResponseEntity<Void> cancelar(String codigoPedido);

}