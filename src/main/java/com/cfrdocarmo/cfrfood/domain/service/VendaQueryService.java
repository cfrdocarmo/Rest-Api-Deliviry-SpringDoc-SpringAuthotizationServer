package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.filter.VendaDiariaFilter;
import com.cfrdocarmo.cfrfood.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filter, String timeOffset);

}
