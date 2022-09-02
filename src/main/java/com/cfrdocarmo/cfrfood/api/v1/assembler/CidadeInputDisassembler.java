package com.cfrdocarmo.cfrfood.api.v1.assembler;

import com.cfrdocarmo.cfrfood.api.v1.model.input.CidadeInput;
import com.cfrdocarmo.cfrfood.domain.model.Cidade;
import com.cfrdocarmo.cfrfood.domain.model.Estado;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CidadeInputDisassembler {

    @Autowired
    private ModelMapper modelMapper;

    public Cidade toDomainObject(CidadeInput cidadeInput) {
        return modelMapper.map(cidadeInput, Cidade.class);
    }

    public void copyToDomainObject(CidadeInput cidadeInput, Cidade cidade) {
        //Para evitar  org.hibernate.HibernateException: identifier of an instance of com.cfrdocarmo.cfrfood.domain.model.Cozinha was altered from 1 to 2
        cidade.setEstado(new Estado());

        modelMapper.map(cidadeInput, cidade);
    }

}
