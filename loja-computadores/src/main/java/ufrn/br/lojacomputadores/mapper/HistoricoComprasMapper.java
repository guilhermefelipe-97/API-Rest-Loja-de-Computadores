package ufrn.br.lojacomputadores.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ufrn.br.lojacomputadores.domain.HistoricoCompras;
import ufrn.br.lojacomputadores.dto.HistoricoComprasRequestDto;
import ufrn.br.lojacomputadores.dto.HistoricoComprasResponseDto;

@Mapper(componentModel = "spring")
public interface HistoricoComprasMapper {
    HistoricoCompras toEntity(HistoricoComprasRequestDto dto);
    HistoricoComprasResponseDto toResponseDto(HistoricoCompras entity);
} 