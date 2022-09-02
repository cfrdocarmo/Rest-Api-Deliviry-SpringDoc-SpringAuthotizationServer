package com.cfrdocarmo.cfrfood.domain.repository;

import com.cfrdocarmo.cfrfood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {

    FotoProduto save(FotoProduto foto);

    void delete(FotoProduto foto);

}
