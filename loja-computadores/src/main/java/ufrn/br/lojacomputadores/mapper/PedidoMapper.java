package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ufrn.br.lojacomputadores.domain.Pedido;
import ufrn.br.lojacomputadores.dto.PedidoRequestDto;
import ufrn.br.lojacomputadores.dto.PedidoResponseDto;

@Mapper(componentModel = "spring", uses = {ClienteMapper.class, ItemPedidoMapper.class})
public interface PedidoMapper {
    
    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "dataPedido", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    Pedido toEntity(PedidoRequestDto dto);
    
    PedidoResponseDto toResponseDto(Pedido entity);
    
    @Mapping(target = "cliente.id", source = "clienteId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "dataPedido", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    void updateEntityFromDto(PedidoRequestDto dto, @MappingTarget Pedido entity);
}

