package com.cfrdocarmo.cfrfood.api.v1.openapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageModelOpenApi<T> {

    private Long size;

    private Long totalElements;

    private Long totalPages;

    private Long number;

}
