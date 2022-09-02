package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.model.PedidoModel;
import com.cfrdocarmo.cfrfood.api.v1.controller.PedidoController;
import com.cfrdocarmo.cfrfood.core.security.CFRdoCarmoSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Pedido;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class PedidoModelAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CFRdoCarmoLinks links;

    @Autowired
    private CFRdoCarmoSecurity cfRdoCarmoSecurity;

    public PedidoModelAssembler() {
        super(PedidoController.class, PedidoModel.class);
    }

    public PedidoModel toModel(Pedido pedido) {
        PedidoModel pedidoModel = createModelWithId(pedido.getId(), pedido);
        modelMapper.map(pedido, pedidoModel);

        pedidoModel.add(links.linkToPedido("pedidos"));

        if( cfRdoCarmoSecurity.podeGerenciarPedidos(pedido.getCodigo()) ) {
            if (pedido.podeSerConfirmado()) {
                pedidoModel.add(links.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
            }

            if (pedido.podeSerCancelado()) {
                pedidoModel.add(links.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
            }

            if (pedido.podeSerEntregue()) {
                pedidoModel.add(links.linkToEntregarPedido(pedido.getCodigo(), "entregar"));
            }
        }

        pedidoModel.getRestaurante().add(links.linkToRestaurante(pedidoModel.getRestaurante().getId()));
        pedidoModel.getCliente().add(links.linkToUsuario(pedidoModel.getCliente().getId()));
        pedidoModel.getFormaPagamento().add(links.linkToRestauranteFormasPagamento(pedidoModel.getFormaPagamento().getId()));
        pedidoModel.getEnderecoEntrega().getCidade().add(links.linkToCidade(pedidoModel.getEnderecoEntrega().getCidade().getId()));

        pedidoModel.getItens().forEach(item -> {
            item.add(links.linkToProduto(pedidoModel.getRestaurante().getId(), item.getProdutoId(), "pruduto"));
        });

        return pedidoModel;
    }



}
