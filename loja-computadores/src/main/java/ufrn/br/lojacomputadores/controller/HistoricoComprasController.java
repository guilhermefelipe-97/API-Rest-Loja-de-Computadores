package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.domain.HistoricoCompras;
import ufrn.br.lojacomputadores.dto.HistoricoComprasRequestDto;
import ufrn.br.lojacomputadores.dto.HistoricoComprasResponseDto;
import ufrn.br.lojacomputadores.mapper.HistoricoComprasMapper;
import ufrn.br.lojacomputadores.service.HistoricoComprasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/historico-compras")
@RequiredArgsConstructor
public class HistoricoComprasController {
    private final HistoricoComprasService historicoComprasService;
    private final HistoricoComprasMapper historicoComprasMapper;

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoComprasResponseDto> buscarPorId(@PathVariable Long id) {
        return historicoComprasService.findById(id)
                .map(historicoComprasMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<HistoricoComprasResponseDto> criar(@Valid @RequestBody HistoricoComprasRequestDto dto) {
        HistoricoCompras historico = historicoComprasMapper.toEntity(dto);
        HistoricoCompras salvo = historicoComprasService.save(historico);
        return ResponseEntity.ok(historicoComprasMapper.toResponseDto(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        historicoComprasService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 