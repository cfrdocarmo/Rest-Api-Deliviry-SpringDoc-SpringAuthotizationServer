package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.exception.EntidadeEmUsoException;
import com.cfrdocarmo.cfrfood.domain.exception.GrupoNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.Grupo;
import com.cfrdocarmo.cfrfood.domain.model.Permissao;
import com.cfrdocarmo.cfrfood.domain.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroGrupoService {

    private static final String MSG_GRUPO_EM_USO = "Grupo de código %d não pode ser removido, pois está em uso";

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CadastroPermissaoService cadastroPermissao;

    @Transactional
    public Grupo salvar(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    @Transactional
    public void associarPermissao(Long grupoId, Long permisssaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permisssaoId);

        grupo.associarPermissao(permissao);
    }

    @Transactional
    public void desassociarPermissao(Long grupoId, Long permisssaoId) {
        Grupo grupo = buscarOuFalhar(grupoId);
        Permissao permissao = cadastroPermissao.buscarOuFalhar(permisssaoId);

        grupo.desassociarPermissao(permissao);
    }

    @Transactional
    public void excluir(Long grupoId) {
        try {
            grupoRepository.deleteById(grupoId);
            grupoRepository.flush();
        } catch (EmptyResultDataAccessException e) {
            throw new GrupoNaoEncontradoException(grupoId);
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, grupoId));
        }
    }

    public Grupo buscarOuFalhar(Long grupoId) {
        return grupoRepository.findById(grupoId)
                .orElseThrow( () -> new GrupoNaoEncontradoException(grupoId));
    }

}
