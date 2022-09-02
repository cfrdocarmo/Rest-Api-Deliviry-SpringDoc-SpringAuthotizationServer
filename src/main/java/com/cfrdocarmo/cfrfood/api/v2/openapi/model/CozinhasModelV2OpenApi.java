package com.cfrdocarmo.cfrfood.api.v2.openapi.model;

import com.cfrdocarmo.cfrfood.api.v2.model.CozinhaModelV2;
import lombok.Data;
import org.springframework.hateoas.Links;

import java.util.List;

@Data
public class CozinhasModelV2OpenApi {

    private CozinhasEmbeddedModelOpenApi _embedded;
    private Links _links;
    private PageModelV2OpenApi page;

    public class CozinhasEmbeddedModelOpenApi {
        private List<CozinhaModelV2> cozinhas;
    }

}
