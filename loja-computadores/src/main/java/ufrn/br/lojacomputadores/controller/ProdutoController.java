package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.dto.ProdutoRequestDto;
import ufrn.br.lojacomputadores.dto.ProdutoResponseDto;
import ufrn.br.lojacomputadores.service.ProdutoService;
import ufrn.br.lojacomputadores.mapper.ProdutoMapper;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;

    @GetMapping
    public List<ProdutoResponseDto> listarTodos() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ProdutoResponseDto buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping
    public ProdutoResponseDto criar(@RequestBody ProdutoRequestDto dto) {
        return produtoService.criar(dto);
    }

    @PutMapping("/{id}")
    public ProdutoResponseDto atualizar(@PathVariable Long id, @RequestBody ProdutoRequestDto dto) {
        return produtoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 