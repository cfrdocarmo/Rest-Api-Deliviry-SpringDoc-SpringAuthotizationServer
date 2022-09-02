package com.cfrdocarmo.cfrfood.api.v1.model.input;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CozinhaIdInput {

    @NotNull
    @Schema(example = "1")
    private Long id;

}
