package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.domain.Admin;
import ufrn.br.lojacomputadores.dto.AdminRequestDto;
import ufrn.br.lojacomputadores.dto.AdminResponseDto;
import ufrn.br.lojacomputadores.mapper.AdminMapper;
import ufrn.br.lojacomputadores.service.AdminService;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @GetMapping
    public Page<AdminResponseDto> listar(Pageable pageable) {
        return adminService.findAll(pageable).map(adminMapper::toResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponseDto> buscarPorId(@PathVariable Long id) {
        return adminService.findById(id)
                .map(adminMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AdminResponseDto> criar(@RequestBody AdminRequestDto dto) {
        Admin admin = adminMapper.toEntity(dto);
        Admin salvo = adminService.save(admin);
        return ResponseEntity.ok(adminMapper.toResponseDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDto> atualizar(@PathVariable Long id, @RequestBody AdminRequestDto dto) {
        Admin admin = adminMapper.toEntity(dto);
        Admin atualizado = adminService.update(id, admin);
        return ResponseEntity.ok(adminMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 