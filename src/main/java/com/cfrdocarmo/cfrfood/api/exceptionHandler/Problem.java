package com.cfrdocarmo.cfrfood.api.exceptionHandler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@Schema(name = "Problema")
public class Problem {

    @Schema(example = "400")
    private Integer status;

    @Schema(example = "https://cfrdocarmo.com.br/dados-invalidos")
    private String type;

    @Schema(example = "Dados inválidos")
    private String title;

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String detail;

    @Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
    private String userMessage;

    @Schema(example = "2022-08-18T23:45:12.902245498Z")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime timestamp;

    @Schema(description = "Lista de objetos ou campos que geraram o erro.")
    private List<Object> objects;

    @Getter
    @Builder
    @Schema(name = "ObjetoProblema")
    public static class Object {

        @Schema(example = "preco")
        private String nome;

        @Schema(example = "O preço é inválido")
        private String userMessage;
    }

}
