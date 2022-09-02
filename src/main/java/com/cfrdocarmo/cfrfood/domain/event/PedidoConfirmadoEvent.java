package com.cfrdocarmo.cfrfood.domain.event;

import com.cfrdocarmo.cfrfood.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PedidoConfirmadoEvent {

    private Pedido pedido;

}
