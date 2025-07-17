package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ufrn.br.lojacomputadores.domain.ItemPedido;
import ufrn.br.lojacomputadores.dto.ItemPedidoResponseDto;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface ItemPedidoMapper {
    
    ItemPedidoResponseDto toResponseDto(ItemPedido entity);
} 