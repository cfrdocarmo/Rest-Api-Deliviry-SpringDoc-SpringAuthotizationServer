package com.cfrdocarmo.cfrfood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "formasPagamento")
@Getter
@Setter
public class FormaPagamentoModel extends RepresentationModel<FormaPagamentoModel> {

    @Schema(example = "1")
    private Long id;

    @Schema(example = "Dinheiro")
    private String descricao;
}
