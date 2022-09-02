package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.model.input.UsuarioComSenhaInput;
import com.cfrdocarmo.cfrfood.api.v1.model.input.UsuarioInput;
import com.cfrdocarmo.cfrfood.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;

@Component
public class UsuarioInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Usuario toDomainObject(@Valid UsuarioComSenhaInput usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }

    public void copyToDomainObject(UsuarioInput usuarioInput, Usuario usuario) {
        //Para evitar  org.hibernate.HibernateException: identifier of an instance of com.cfrdocarmo.cfrfood.domain.model.Cozinha was altered from 1 to 2

        modelMapper.map(usuarioInput, usuario);
    }

}
