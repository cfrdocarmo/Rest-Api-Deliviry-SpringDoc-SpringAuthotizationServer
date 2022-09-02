package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class RootEntryPointcontroller {

    @Autowired
    private CFRdoCarmoLinks links;

    @GetMapping
    @Operation(hidden = true)
    public RootEntryPointModel root() {
        var rootEntryPointModel = new RootEntryPointModel();

        rootEntryPointModel.add(links.linkToCozinhas("cozinhas"));
        rootEntryPointModel.add(links.linkToPedidos("pedidos"));
        rootEntryPointModel.add(links.linkToRestaurantes("restaurantes"));
        rootEntryPointModel.add(links.linkToGrupos("grupos"));
        rootEntryPointModel.add(links.linkToUsuarios("usuarios"));
        rootEntryPointModel.add(links.linkToPermissoes("permissoes"));
        rootEntryPointModel.add(links.linkToFormasPagamento("formas-pagamento"));
        rootEntryPointModel.add(links.linkToEstados("estados"));
        rootEntryPointModel.add(links.linkToCidades("cidades"));
        rootEntryPointModel.add(links.linkToEstatisticas("estatisticas"));

        return rootEntryPointModel;
    }

    private static class RootEntryPointModel extends RepresentationModel<RootEntryPointModel> {

    }

}
