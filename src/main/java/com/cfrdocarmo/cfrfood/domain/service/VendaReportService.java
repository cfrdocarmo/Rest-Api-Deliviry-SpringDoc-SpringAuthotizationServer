package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.filter.VendaDiariaFilter;

public interface VendaReportService {

    byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset);

}
