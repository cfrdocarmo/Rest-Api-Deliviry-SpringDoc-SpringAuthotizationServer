package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.controller.EstadoController;
import com.cfrdocarmo.cfrfood.api.v1.model.EstadoModel;
import com.cfrdocarmo.cfrfood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class EstadoModelAssembler extends RepresentationModelAssemblerSupport<Estado, EstadoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CFRdoCarmoLinks links;

    public EstadoModelAssembler() {
        super(EstadoController.class, EstadoModel.class);
    }

    public EstadoModel toModel(Estado estado) {
        EstadoModel estadoModel = createModelWithId(estado.getId(), estado);
        modelMapper.map(estado, estadoModel);

        estadoModel.add( links.linkToEstados("estados"));

        return estadoModel;
    }

    @Override
    public CollectionModel<EstadoModel> toCollectionModel(Iterable<? extends Estado> entities) {
        return super.toCollectionModel(entities).add(links.linkToEstados());
    }
}
