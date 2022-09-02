package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.CFRdoCarmoLinks;
import com.cfrdocarmo.cfrfood.api.v1.controller.UsuarioController;
import com.cfrdocarmo.cfrfood.api.v1.model.UsuarioModel;
import com.cfrdocarmo.cfrfood.domain.model.Usuario;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class UsuarioModelAssembler extends RepresentationModelAssemblerSupport<Usuario, UsuarioModel> {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CFRdoCarmoLinks links;

    public UsuarioModelAssembler() {
        super(UsuarioController.class, UsuarioModel.class);
    }

    public UsuarioModel toModel(Usuario usuario) {
        UsuarioModel usuarioModel = createModelWithId(usuario.getId(), usuario);
        modelMapper.map(usuario, usuarioModel);

        usuarioModel.add(links.linkToUsuarios("usuarios"));
        usuarioModel.add(links.linkToGruposUsuario(usuario.getId(), "grupo-usuários"));

        return usuarioModel;
    }

    @Override
    public CollectionModel<UsuarioModel> toCollectionModel(Iterable<? extends Usuario> entities) {
        return super.toCollectionModel(entities).add(links.linkToUsuarios());
    }
}
