package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.controller.RestauranteProdutoController;
import com.cfrdocarmo.cfrfood.api.v1.model.ProdutoModel;
import com.cfrdocarmo.cfrfood.domain.model.Produto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class ProdutoModelAssemble extends RepresentationModelAssemblerSupport<Produto, ProdutoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CFRdoCarmoLinks links;

    public ProdutoModelAssemble() {
        super(RestauranteProdutoController.class, ProdutoModel.class);
    }

    @Override
    public ProdutoModel toModel(Produto produto) {
        ProdutoModel produtoModel = createModelWithId(
                produto.getId(), produto, produto.getRestaurante().getId());

        modelMapper.map(produto, produtoModel);

        produtoModel.add(links.linkToProdutos(produto.getRestaurante().getId(), "produtos"));

        produtoModel.add(links.linkToFotoProduto(
                produto.getRestaurante().getId(), produto.getId(), "foto"));

        return produtoModel;
    }

}
