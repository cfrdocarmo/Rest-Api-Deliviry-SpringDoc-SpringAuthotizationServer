package com.cfrdocarmo.cfrfood.api.exceptionHandler;

import lombok.Getter;

@Getter
public enum ProblemType {

    MENSAGEM_INCOMPREENSIVEL("/mensagem-incompreensivel", "Mensagem incompreensível"),
    PARAMETRO_INVALIDO("/parametro-invalido", "Parametro inválido."),
    DADOS_INVALIDOS("/dados-invalidos", "Dados inválidos."),
    ERRO_DE_SISTEMA("/erro-de-sistema", "Erro de sistema."),
    RECURSO_NAO_ENCONTRADO("/recurso-nao-encontrada", "Recurso não encontrada"),
    ENTIDADE_EM_USO("/entidade-em-uso", "Entidade em uso"),
    ERRO_NEGOCIO("/erro-negocio", "Violação de regra de negócio"),
    ACESSO_NEGADO("acesso-negado", "Acesso Negado");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "https://cfrdocarmo.com.br" + path;
        this.title = title;
    }
}
