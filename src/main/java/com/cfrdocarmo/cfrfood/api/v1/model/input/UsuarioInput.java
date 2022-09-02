package com.cfrdocarmo.cfrfood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UsuarioInput{

    @Schema(example = "Carlos do Carmo")
    @NotBlank
    private String nome;

    @Schema(example = "cfrdocarmo.ger@gmail.com")
    @NotBlank
    @Email
    private String email;

}
