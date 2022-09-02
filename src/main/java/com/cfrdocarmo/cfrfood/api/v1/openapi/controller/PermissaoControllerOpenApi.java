package com.cfrdocarmo.cfrfood.api.v1.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v1.model.PermissaoModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Permissões")
public interface PermissaoControllerOpenApi {

    @Operation(summary = "Lista as permissões")
    public CollectionModel<PermissaoModel> listar();

}