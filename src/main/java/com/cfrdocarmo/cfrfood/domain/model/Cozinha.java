package com.cfrdocarmo.cfrfood.domain.model;

import com.cfrdocarmo.cfrfood.core.validation.Groups;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@JsonRootName("cozinha")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {

	@EqualsAndHashCode.Include
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = Groups.CozinhaId.class)
	private Long id;

	//@JsonProperty("titulo")
	//@NotBlank
	@Column(nullable = false)
	private String nome;

	@OneToMany(mappedBy = "cozinha")
	private List<Restaurante> restaurantes = new ArrayList<>();

}
