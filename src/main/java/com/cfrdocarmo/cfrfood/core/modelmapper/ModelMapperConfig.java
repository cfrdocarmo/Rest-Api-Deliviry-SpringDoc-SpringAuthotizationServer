package com.cfrdocarmo.cfrfood.core.modelmapper;

import com.cfrdocarmo.cfrfood.api.v1.model.EnderecoModel;
import com.cfrdocarmo.cfrfood.api.v1.model.input.ItemPedidoInput;
import com.cfrdocarmo.cfrfood.api.v2.model.input.CidadeInputV2;
import com.cfrdocarmo.cfrfood.domain.model.Cidade;
import com.cfrdocarmo.cfrfood.domain.model.Endereco;
import com.cfrdocarmo.cfrfood.domain.model.ItemPedido;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {

        var modelMapper = new ModelMapper();

        /*
        var modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Restaurante.class, RestauranteModel.class)
                .addMapping((Restaurante::getTaxaFrete), RestauranteModel::setTaxaFrete);
        */

        modelMapper.createTypeMap(CidadeInputV2.class, Cidade.class)
                .addMappings(mapper -> mapper.skip(Cidade::setId));

        modelMapper.createTypeMap(ItemPedidoInput.class, ItemPedido.class)
                .addMappings(mapper -> mapper.skip(ItemPedido::setId));

        var enderecoToEnderecoModelTypeMap = modelMapper.createTypeMap(
                Endereco.class, EnderecoModel.class);

        enderecoToEnderecoModelTypeMap.<String>addMapping(
                enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(),
                (enderecoModelDest, value) -> enderecoModelDest.getCidade().setEstado(value));

        return  modelMapper;
    }
}
