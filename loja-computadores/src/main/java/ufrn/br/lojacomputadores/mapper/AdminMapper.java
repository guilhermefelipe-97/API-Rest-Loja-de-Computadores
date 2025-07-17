package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ufrn.br.lojacomputadores.domain.Admin;
import ufrn.br.lojacomputadores.dto.AdminRequestDto;
import ufrn.br.lojacomputadores.dto.AdminResponseDto;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    Admin toEntity(AdminRequestDto dto);

    AdminResponseDto toResponseDto(Admin entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromDto(AdminRequestDto dto, @MappingTarget Admin entity);
} 