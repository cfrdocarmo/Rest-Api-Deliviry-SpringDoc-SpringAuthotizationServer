package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.assembler.FormaPagamentoAssembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.FormaPagamentoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.model.FormaPagamentoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.FormaPagamentoInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.FormasPagamentoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.FormaPagamento;
import com.cfrdocarmo.cfrfood.domain.repository.FormaPagamentoRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroFormaPagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(path ="/v1/formas-pagamento", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormaPagamentoController implements FormasPagamentoControllerOpenApi {

    @Autowired
    private FormaPagamentoAssembler formaPagamentoAssembler;

    @Autowired
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();

        if(dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if(request.checkNotModified(eTag)) {
            return null;
        }

        List<FormaPagamento> formaPagamento = formaPagamentoRepository.findAll();

        CollectionModel<FormaPagamentoModel> todasFormasPagamento = formaPagamentoAssembler.toCollectionModel(formaPagamento);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .eTag(eTag)
                .body(todasFormasPagamento);
    }

    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping("/{formaPagamentoId}")
    public ResponseEntity<FormaPagamentoModel> buscarPorID(@PathVariable Long formaPagamentoId, ServletWebRequest request) {

        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacaoById(formaPagamentoId);

        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        FormaPagamentoModel formaPagamentoModel = formaPagamentoAssembler.toModel(formaPagamento);

        return ResponseEntity.ok()
//                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//                .cacheControl(CacheControl.noCache())
//                .cacheControl(CacheControl.noStore())
                .body(formaPagamentoModel);
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FormaPagamentoModel adicionar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput){
        FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);

        formaPagamento = cadastroFormaPagamento.salvar(formaPagamento);

        return formaPagamentoAssembler.toModel(formaPagamento);
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PutMapping("/{formaPagamentoId}")
    public FormaPagamentoModel atualizar(@PathVariable Long formaPagamentoId, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        FormaPagamento formaPagamentoAtual = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

        return formaPagamentoAssembler.toModel(cadastroFormaPagamento.salvar(formaPagamentoAtual));
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long formaPagamentoId) {
        cadastroFormaPagamento.excluir(formaPagamentoId);
    }


}
