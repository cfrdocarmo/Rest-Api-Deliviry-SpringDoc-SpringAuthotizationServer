package com.cfrdocarmo.cfrfood.api.v2.openapi.controller;

import com.cfrdocarmo.cfrfood.api.exceptionHandler.Problem;
import com.cfrdocarmo.cfrfood.api.v2.model.CozinhaModelV2;
import com.cfrdocarmo.cfrfood.api.v2.model.input.CozinhaInputV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

public interface CozinhaControllerV2OpenApi {

    public PagedModel<CozinhaModelV2> listar(@PageableDefault(size = 10) Pageable pageable);

    public CozinhaModelV2 buscarPorId(Long cozinhaId);

    public CozinhaModelV2 adicionar(CozinhaInputV2 cozinhaInput);

    public CozinhaModelV2 atualizar(CozinhaInputV2 cozinhaInput);

    public void remover(Long cozinhaId);


}
