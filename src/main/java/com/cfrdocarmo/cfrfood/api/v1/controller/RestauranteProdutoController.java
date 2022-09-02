package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.assembler.ProdutoInputDisassembler;
import com.cfrdocarmo.cfrfood.api.v1.assembler.ProdutoModelAssemble;
import com.cfrdocarmo.cfrfood.api.v1.model.ProdutoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.ProdutoInput;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.RestauranteProdutoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Produto;
import com.cfrdocarmo.cfrfood.domain.model.Restaurante;
import com.cfrdocarmo.cfrfood.domain.repository.ProdutoRepository;
import com.cfrdocarmo.cfrfood.domain.service.CadastroProdutoService;
import com.cfrdocarmo.cfrfood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos")
public class RestauranteProdutoController implements RestauranteProdutoControllerOpenApi {

    @Autowired
    private CadastroProdutoService cadastroProduto;

    @Autowired
    private CadastroRestauranteService cadastroRestaurante;

    @Autowired
    private ProdutoModelAssemble produtoModelAssemble;

    @Autowired
    private ProdutoInputDisassembler produtoInputDisassembler;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CFRdoCarmoLinks links;

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping
    public CollectionModel<ProdutoModel> listar(@PathVariable Long restauranteId, @RequestParam(required = false, defaultValue = "false") Boolean incluirInativos) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        List<Produto> todosProdutos = null;

        if(incluirInativos == true) {
            todosProdutos = produtoRepository.findTodosByRestaurante(restaurante);
        } else {
            todosProdutos = produtoRepository.findByAtivoRestaurante(restaurante);
        }

        return produtoModelAssemble.toCollectionModel(todosProdutos).add(links.linkToProdutos(restauranteId));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{produtoId}")
    public ProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

        Produto produto = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        return produtoModelAssemble.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);

        produto.setRestaurante(restaurante);

        produto = cadastroProduto.salvar(produto);

        return produtoModelAssemble.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long restauranteId, @PathVariable Long produtoId ,
                                  @RequestBody @Valid ProdutoInput produtoInput) {

        Produto produtoAtual = cadastroProduto.buscarOuFalhar(restauranteId, produtoId);

        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        produtoAtual = cadastroProduto.salvar(produtoAtual);

        return produtoModelAssemble.toModel(produtoAtual);
    }

}
