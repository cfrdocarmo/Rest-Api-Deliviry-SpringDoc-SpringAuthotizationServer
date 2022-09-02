package com.cfrdocarmo.cfrfood.domain.listener;

import com.cfrdocarmo.cfrfood.domain.event.PedidoCanceladoEvent;
import com.cfrdocarmo.cfrfood.domain.event.PedidoConfirmadoEvent;
import com.cfrdocarmo.cfrfood.domain.model.Pedido;
import com.cfrdocarmo.cfrfood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmail;

    @TransactionalEventListener()
    public void aoCancelarPedido(PedidoCanceladoEvent event) {
        Pedido pedido = event.getPedido();

        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
                .corpo("pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();

        envioEmail.enviar(mensagem);
    }

}
