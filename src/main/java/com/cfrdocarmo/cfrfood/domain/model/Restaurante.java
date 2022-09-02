package com.cfrdocarmo.cfrfood.domain.model;


import com.cfrdocarmo.cfrfood.core.validation.Groups;
import com.cfrdocarmo.cfrfood.core.validation.ValorZeroIncluiDescricao;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoria = "Frete Grátis")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@NotBlank
	private String nome;

	@NotNull
	@PositiveOrZero
	@Column(name = "taxa_frete", nullable = false)
	private BigDecimal taxaFrete;
	
	@Valid
	@ConvertGroup(from = Default.class, to = Groups.CozinhaId.class)
	@NotNull()
	@ManyToOne //(fetch = FetchType.LAZY)
	@JoinColumn(name = "cozinha_id", nullable = false)
	private Cozinha cozinha;
	
	@Embedded
	private Endereco endereco;

	private Boolean ativo = Boolean.TRUE;

	private Boolean aberto = Boolean.FALSE;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataCadastro;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "datetime")
	private OffsetDateTime dataAtualizacao;
	
	@ManyToMany
	@JoinTable(name = "restaurante_forma_pagamento",
			joinColumns = @JoinColumn(name = "restaurante_id"),
			inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
	private Set<FormaPagamento> formasPagamento = new HashSet<>();

	@ManyToMany
	@JoinTable(name = "restaurante_usuario_responsavel",
	        joinColumns = @JoinColumn(name = "restaurante_id"),
	        inverseJoinColumns = @JoinColumn(name = "usuario_id"))
	private Set<Usuario> responsaveis = new HashSet<>();
	
	@OneToMany(mappedBy = "restaurante")
	private List<Produto> produtos = new ArrayList<>();

	public void ativar() {
		setAtivo(true);
	}

	public void inativar() {
		setAtivo(false);
	}

	public void abrir() {
		setAberto(true);
	}

	public void fechar() {
		setAberto(false);
	}

	public boolean desassociarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().remove(formaPagamento);
	}

	public boolean associarFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().add(formaPagamento);
	}

	public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
		return getFormasPagamento().contains(formaPagamento);
	}

	public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
		return !aceitaFormaPagamento(formaPagamento);
	}

	public boolean associarUsuario(Usuario usuario) {
		return this.getResponsaveis().add(usuario);
	}

	public boolean desassociarUsuario(Usuario usuario) {
		return this.getResponsaveis().remove(usuario);
	}

	public boolean removerResponsavel(Usuario usuario)  {
		return  this.getResponsaveis().remove(usuario);
	}

	public boolean adicionarResponsavel(Usuario usuario)  {
		return  this.getResponsaveis().add(usuario);
	}

	public boolean isAberto() {
		return this.aberto;
	}

	public boolean isFechado() {
		return !isAberto();
	}

	public boolean isAtivo() {
		return this.ativo;
	}

	public boolean isInativo() {
		return !isAtivo();
	}

	public boolean aberturaPermitida() {
		return isAtivo() && isFechado();
	}

	public boolean ativacaoPermitida() {
		return isInativo();
	}

	public boolean inativacaoPermitida() {
		return isAtivo();
	}

	public boolean fechamentoPermitido() {
		return isAberto();
	}
	
}