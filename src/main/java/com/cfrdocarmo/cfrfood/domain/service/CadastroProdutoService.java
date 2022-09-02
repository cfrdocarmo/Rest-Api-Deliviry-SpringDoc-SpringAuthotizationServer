package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.exception.ProdutoNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.Produto;
import com.cfrdocarmo.cfrfood.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto) {
       return produtoRepository.save(produto);
    }

    public Produto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findById(restauranteId, produtoId)
                .orElseThrow( () -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }


}
