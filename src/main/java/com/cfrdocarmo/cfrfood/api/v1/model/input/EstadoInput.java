package com.cfrdocarmo.cfrfood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EstadoInput {

    @Schema(example = "Ceara")
    @NotBlank
    private String nome;
}
