package com.cfrdocarmo.cfrfood.api.v1.openapi.model;

import com.cfrdocarmo.cfrfood.api.v1.model.EstadoModel;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class EstadosModelOpenApi {

    private EstadosEmbeddedModelOpenApi _embedded;
    private Links _links;

    @Data
    public class EstadosEmbeddedModelOpenApi {

        private List<EstadoModel> estados;

    }
}
