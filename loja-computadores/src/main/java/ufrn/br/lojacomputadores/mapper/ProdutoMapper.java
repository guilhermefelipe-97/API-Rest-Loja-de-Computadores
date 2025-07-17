package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ufrn.br.lojacomputadores.domain.Produto;
import ufrn.br.lojacomputadores.dto.ProdutoRequestDto;
import ufrn.br.lojacomputadores.dto.ProdutoResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    Produto toEntity(ProdutoRequestDto dto);
    
    ProdutoResponseDto toResponseDto(Produto entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    void updateEntityFromDto(ProdutoRequestDto dto, @MappingTarget Produto entity);
}

