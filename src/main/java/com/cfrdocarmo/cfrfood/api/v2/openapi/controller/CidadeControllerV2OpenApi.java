package com.cfrdocarmo.cfrfood.api.v2.openapi.controller;

import com.cfrdocarmo.cfrfood.api.v2.model.CidadeModelV2;
import com.cfrdocarmo.cfrfood.api.v2.model.input.CidadeInputV2;
import org.springframework.hateoas.CollectionModel;

public interface CidadeControllerV2OpenApi {

    public CollectionModel<CidadeModelV2> listar();

    public CidadeModelV2 buscarPorId(Long cidadeId);

    public CidadeModelV2 adicionar(CidadeInputV2 cidadeInput);

    public CidadeModelV2 atualizar(CidadeInputV2 cidadeInput);

    public void remover(Long cidadeId);
    
}
