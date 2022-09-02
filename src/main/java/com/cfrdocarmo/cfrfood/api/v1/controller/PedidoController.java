package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.PedidoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.PedidoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.PedidoResumoModelAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.PedidoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.PedidoResumoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.PedidoInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.PedidoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.data.PageWrapper;
import com.cfrdocarmo.cfrfood.core.data.PageableTranslator;
import com.cfrdocarmo.cfrfood.core.security.CFRdoCarmoSecurity;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.exception.EntidadeNaoEncontradaException;
import com.cfrdocarmo.cfrfood.domain.exception.NegocioException;
import com.cfrdocarmo.cfrfood.domain.model.Pedido;
import com.cfrdocarmo.cfrfood.domain.model.Usuario;
import com.cfrdocarmo.cfrfood.domain.repository.PedidoRepository;
import com.cfrdocarmo.cfrfood.domain.filter.PedidoFilter;
import com.cfrdocarmo.cfrfood.domain.service.EmissaoPedidoService;
import com.cfrdocarmo.cfrfood.infrastructure.repository.spec.PedidoSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController implements PedidoControllerOpenApi {

    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;

    @Autowired
    private EmissaoPedidoService emissaoPedido;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;

    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;

    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;

    @Autowired
    private CFRdoCarmoSecurity cfRdoCarmoSecurity;

    @CheckSecurity.Pedidos.PodePesquisar
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro,
                                                   @PageableDefault(size = 10) Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpec.usandoFiltro(filtro), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        PagedModel<PedidoResumoModel> pedidoResumoModelPagedModel = pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);

        return pedidoResumoModelPagedModel;
    }

    @CheckSecurity.Pedidos.PodeBuscar
    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable String codigoPedido) {
        System.out.println(cfRdoCarmoSecurity.getUsuarioId());
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

        return pedidoModelAssembler.toModel(pedido);
    }

    @CheckSecurity.Pedidos.PodeCriar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
        try{
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);


            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(cfRdoCarmoSecurity.getUsuarioId());

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    public Pageable traduzirPageable(Pageable apiPageable) {

        var mapeamento = Map.of(
                "codigo", "codigo",
                "subtotal", "subtotal",
                "taxaFrete", "taxaFrete",
                "dataCriacao", "dataCriacao",
                "restaurante.nome", "restaurante.nome",
                "restaurante.id", "restaurante.id",
                "cliente.id", "cliente.id",
                "cliente.nome", "cliente.nome"
        );

        return PageableTranslator.translator(apiPageable, mapeamento);
    }



}
