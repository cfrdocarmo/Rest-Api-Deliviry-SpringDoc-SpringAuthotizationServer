package com.cfrdocarmo.cfrfood.api.v1.controller;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.assembler.FormaPagamentoAssembler;
import com.cfrdocarmo.cfrfood.api.v1.model.FormaPagamentoModel;
import com.cfrdocarmo.cfrfood.api.v1.openapi.controller.RestaurantesFormasPagamentoControllerOpenApi;
import com.cfrdocarmo.cfrfood.core.security.CheckSecurity;
import com.cfrdocarmo.cfrfood.domain.model.Restaurante;
import com.cfrdocarmo.cfrfood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/restaurantes/{restauranteId}/formas-pagamento")
public class RestauranteFormaPagamentoController implements RestaurantesFormasPagamentoControllerOpenApi {

	@Autowired
	private CadastroRestauranteService cadastroRestaurante;

	@Autowired
	private FormaPagamentoAssembler formaPagamentoAssembler;

	@Autowired
	private CFRdoCarmoLinks links;

	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {

		Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

		CollectionModel<FormaPagamentoModel> formasPagamentoModel = formaPagamentoAssembler.toCollectionModel(restaurante.getFormasPagamento())
				.removeLinks()
				.add(links.linkToRestauranteFormasPagamento(restauranteId))
				.add(links.linkToRestauranteFormaPagamentoAssociacao(restauranteId, "associar"));

		formasPagamentoModel.getContent().forEach(formaPagamentoModel -> {
			formaPagamentoModel.add(links.linkToRestauranteFormaPagamentoDesassociacao(restauranteId, formaPagamentoModel.getId(), "desassociar"));
		});

		return formasPagamentoModel;
	}

	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@DeleteMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);

		return ResponseEntity.noContent().build();
	}

	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("/{formaPagamentoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId) {
		cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);

		return ResponseEntity.noContent().build();
	}

}
