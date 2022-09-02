package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.exception.EntidadeEmUsoException;
import com.cfrdocarmo.cfrfood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.FormaPagamento;
import com.cfrdocarmo.cfrfood.domain.repository.FormaPagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroFormaPagamentoService {

    private static final String MSG_FORMAPAGAMENTO_EM_USO = "Forma de Pagamento de código %d não pode ser removida, pois esta em uso";

    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }

    @Transactional
    public void excluir(Long formaPagamentoId) {
        try {
            formaPagamentoRepository.deleteById(formaPagamentoId);
            formaPagamentoRepository.flush();

        } catch (EmptyResultDataAccessException e) {
            throw new FormaPagamentoNaoEncontradoException(formaPagamentoId);

        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_FORMAPAGAMENTO_EM_USO, formaPagamentoId));
        }
    }

    public FormaPagamento buscarOuFalhar(Long formaPagamentoId){
        return formaPagamentoRepository.findById(formaPagamentoId)
                .orElseThrow( () -> new FormaPagamentoNaoEncontradoException(formaPagamentoId));
    }
}
