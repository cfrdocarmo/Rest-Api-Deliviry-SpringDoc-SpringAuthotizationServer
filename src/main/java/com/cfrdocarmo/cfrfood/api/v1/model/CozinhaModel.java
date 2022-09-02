package com.cfrdocarmo.cfrfood.api.v1.model;

import com.cfrdocarmo.cfrfood.api.v1.model.view.RestauranteView;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "cozinhas")
@Setter
@Getter
public class CozinhaModel extends RepresentationModel<CozinhaModel> {

    @JsonView(RestauranteView.Resumo.class)
    @Schema(example = "1")
    private Long id;

    @JsonView(RestauranteView.Resumo.class)
    @Schema(example = "Japonêsa")
    private String nome;

}
