package com.cfrdocarmo.cfrfood.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ItemPedido {
 
	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Integer quantidade;
	
	@Column(nullable = false)
	private BigDecimal precoUnitario;
	
	@Column(nullable = false)
	private BigDecimal precoTotal;
	
	private String observacao;
	
	@ManyToOne
	@JoinColumn(nullable =false)
	private Pedido pedido;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Produto produto;

	public void calculaPrecoTotal() {
		BigDecimal precoUnitario = getPrecoUnitario();
		Integer quantidade = getQuantidade();

		if(precoUnitario == null) {
			precoUnitario = BigDecimal.ZERO;
		}

		if(quantidade == null) {
			quantidade = 0;
		}

		this.setPrecoTotal(precoUnitario.multiply(new BigDecimal(quantidade)));
	}
}
