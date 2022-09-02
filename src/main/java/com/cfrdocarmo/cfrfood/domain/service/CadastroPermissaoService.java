package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.exception.EntidadeEmUsoException;
import com.cfrdocarmo.cfrfood.domain.exception.PermissaoNaoEncontradaException;
import com.cfrdocarmo.cfrfood.domain.exception.RestauranteNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.Permissao;
import com.cfrdocarmo.cfrfood.domain.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroPermissaoService {

    private static final String MSG_PERMISSAO_EM_USO = "Restaurante de código %d, não pode ser removido, pois está em uso.";


    @Autowired
    private PermissaoRepository permissaoRepository;

    @Transactional
    public Permissao salvar(Permissao permissao) {
        return permissaoRepository.save(permissao);
    }

    @Transactional
    public void excluir(Long permissaoId) {
        try {
            permissaoRepository.deleteById(permissaoId);
            permissaoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new RestauranteNaoEncontradoException(permissaoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_PERMISSAO_EM_USO, permissaoId));
        }
    }

    public Permissao buscarOuFalhar(Long permissaoId) {
        return permissaoRepository.findById(permissaoId)
                .orElseThrow( () -> new PermissaoNaoEncontradaException(permissaoId));
    }

}
