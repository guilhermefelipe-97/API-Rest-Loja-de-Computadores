package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ufrn.br.lojacomputadores.domain.Cliente;
import ufrn.br.lojacomputadores.dto.ClienteRequestDto;
import ufrn.br.lojacomputadores.dto.ClienteResponseDto;
import ufrn.br.lojacomputadores.dto.HistoricoComprasResponseDto;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    Cliente toEntity(ClienteRequestDto dto);
    
    ClienteResponseDto toResponseDto(Cliente entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    void updateEntityFromDto(ClienteRequestDto dto, @MappingTarget Cliente entity);
}

