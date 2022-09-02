package com.cfrdocarmo.cfrfood.domain.exception;

public class FotoProdutoNaoEncotradoException extends EntidadeNaoEncontradaException {
    private static final long serialVersionUID = 1L;

    public FotoProdutoNaoEncotradoException(String mensagem) {
        super(mensagem);
    }

    public FotoProdutoNaoEncotradoException(Long restauranteId, Long produtoId) {
        this(String.format("Não existe uma foto do produto com código %d para o restaurante de código %d",
                produtoId, restauranteId));
    }
}
