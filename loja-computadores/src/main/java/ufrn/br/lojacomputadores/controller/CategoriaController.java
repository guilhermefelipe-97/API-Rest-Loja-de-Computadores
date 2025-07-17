package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.dto.CategoriaRequestDto;
import ufrn.br.lojacomputadores.dto.CategoriaResponseDto;
import ufrn.br.lojacomputadores.service.CategoriaService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    
    private final CategoriaService categoriaService;
    
    @GetMapping
    public List<CategoriaResponseDto> listarTodos() {
        return categoriaService.listarTodos();
    }
    
    @GetMapping("/{id}")
    public CategoriaResponseDto buscarPorId(@PathVariable Long id) {
        return categoriaService.buscarPorId(id);
    }
    
    @GetMapping("/nome/{nome}")
    public CategoriaResponseDto buscarPorNome(@PathVariable String nome) {
        return categoriaService.buscarPorNome(nome);
    }
    
    @PostMapping
    public CategoriaResponseDto criar(@Valid @RequestBody CategoriaRequestDto dto) {
        return categoriaService.criar(dto);
    }
    
    @PutMapping("/{id}")
    public CategoriaResponseDto atualizar(@PathVariable Long id, 
                                         @Valid @RequestBody CategoriaRequestDto dto) {
        return categoriaService.atualizar(id, dto);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 