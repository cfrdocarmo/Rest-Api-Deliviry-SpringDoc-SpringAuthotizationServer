package com.cfrdocarmo.cfrfood.domain.service;

import com.cfrdocarmo.cfrfood.domain.exception.NegocioException;
import com.cfrdocarmo.cfrfood.domain.exception.UsuarioNaoEncontradoException;
import com.cfrdocarmo.cfrfood.domain.model.Grupo;
import com.cfrdocarmo.cfrfood.domain.model.Usuario;
import com.cfrdocarmo.cfrfood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CadastroUsuarioService {

    private static final String MSG_USUARIO_EM_USO = "Usuário de código %d não pode ser removido, pois está em uso";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CadastroGrupoService cadastroGrupo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {

        usuarioRepository.detach(usuario);

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

        if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new NegocioException(
                    String.format("Já existe um usuário cadastrado com o email %s", usuario.getEmail()));
        }

        if (usuario.isNovo()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long usuarioId, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarOuFalhar(usuarioId);

        if(!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new NegocioException("Senha atual não coincide com a senha do usuário.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));

    }

    @Transactional
    public void adicionarGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.adicionarGrupo(grupo);
    }

    @Transactional
    public void removerGrupo(Long usuarioId, Long grupoId) {
        Usuario usuario = buscarOuFalhar(usuarioId);
        Grupo grupo = cadastroGrupo.buscarOuFalhar(grupoId);

        usuario.removerGrupo(grupo);
    }

    public Usuario buscarOuFalhar(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow( () -> new UsuarioNaoEncontradoException(usuarioId));
    }

//    @Transactional
//    public void excluir(Long usuarioId) {
//        try {
//            usuarioRepository.deleteById(usuarioId);
//            usuarioRepository.flush();
//        } catch (EmptyResultDataAccessException e) {
//            throw new UsuarioNaoEncontradoException(usuarioId);
//        } catch (DataIntegrityViolationException e) {
//            throw new EntidadeEmUsoException(String.format(MSG_USUARIO_EM_USO, usuarioId));
//        }
//    }

}
