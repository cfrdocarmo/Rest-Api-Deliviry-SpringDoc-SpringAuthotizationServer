package com.cfrdocarmo.cfrfood.api.v2.assembler;

import com.cfrdocarmo.cfrfood.api.v2.CFRdoCarmoLinksV2;
import com.cfrdocarmo.cfrfood.api.v2.controller.CozinhaControllerV2;
import com.cfrdocarmo.cfrfood.api.v2.model.CozinhaModelV2;
import com.cfrdocarmo.cfrfood.domain.model.Cozinha;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CozinhaModelAssemblerV2 extends RepresentationModelAssemblerSupport<Cozinha, CozinhaModelV2> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    CFRdoCarmoLinksV2 links;

    public CozinhaModelAssemblerV2() {
        super(CozinhaControllerV2.class, CozinhaModelV2.class);
    }

    @Override
    public CozinhaModelV2 toModel(Cozinha cozinha) {
        CozinhaModelV2 cozinhaModel = createModelWithId(cozinha.getId(), cozinha);
        modelMapper.map(cozinha, cozinhaModel);

        cozinhaModel.add(links.linkToCozinhas("cozinhas"));

        return cozinhaModel;
    }



}
