package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.controller.RestauranteController;
import com.cfrdocarmo.cfrfood.api.v1.model.RestauranteModel;
import com.cfrdocarmo.cfrfood.domain.model.Restaurante;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CFRdoCarmoLinks links;

    public RestauranteModelAssembler() {
        super(RestauranteController.class, RestauranteModel.class);
    }

    public RestauranteModel toModel(Restaurante restaurante) {
        RestauranteModel restauranteModel = createModelWithId(restaurante.getId(), restaurante);
        modelMapper.map(restaurante, restauranteModel);

        if (restaurante.ativacaoPermitida()) {
            restauranteModel.add(links.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
        }

        if (restaurante.inativacaoPermitida()) {
            restauranteModel.add(links.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
        }

        if (restaurante.aberturaPermitida()) {
            restauranteModel.add(links.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
        }

        if (restaurante.fechamentoPermitido()) {
            restauranteModel.add(links.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
        }

        restauranteModel.add(links.linkToProdutos(restaurante.getId(), "produtos"));
        restauranteModel.add(links.linkToRestaurantes());

        if (restauranteModel.getEndereco() != null
                && restauranteModel.getEndereco().getCidade() != null) {
            restauranteModel.getEndereco().getCidade().add(links.linkToCidade(restaurante.getEndereco().getCidade().getId()));
        }
        restauranteModel.getCozinha().add(links.linkToCozinha(restaurante.getCozinha().getId()));
        restauranteModel.add(links.linkToRestauranteFormasPagamento(restaurante.getId()));
        restauranteModel.add(links.linkToResponsaveisRestaurante(restaurante.getId()));

        return restauranteModel;
    }

    @Override
    public CollectionModel<RestauranteModel> toCollectionModel(Iterable<? extends Restaurante> entities) {
        return super.toCollectionModel(entities).add(linkTo(RestauranteController.class).withSelfRel());
    }
}
