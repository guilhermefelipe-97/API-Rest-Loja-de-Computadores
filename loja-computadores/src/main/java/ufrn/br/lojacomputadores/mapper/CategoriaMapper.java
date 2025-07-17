package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ufrn.br.lojacomputadores.domain.Categoria;
import ufrn.br.lojacomputadores.dto.CategoriaRequestDto;
import ufrn.br.lojacomputadores.dto.CategoriaResponseDto;

@Mapper(componentModel = "spring", uses = {ProdutoMapper.class})
public interface CategoriaMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    Categoria toEntity(CategoriaRequestDto dto);
    
    CategoriaResponseDto toResponseDto(Categoria entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "produtos", ignore = true)
    void updateEntityFromDto(CategoriaRequestDto dto, @MappingTarget Categoria entity);
} 