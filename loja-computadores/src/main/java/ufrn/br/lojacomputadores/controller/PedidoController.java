package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.dto.PedidoRequestDto;
import ufrn.br.lojacomputadores.dto.PedidoResponseDto;
import ufrn.br.lojacomputadores.service.PedidoService;
import ufrn.br.lojacomputadores.mapper.PedidoMapper;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    @GetMapping
    public List<PedidoResponseDto> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PedidoResponseDto buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @PostMapping
    public PedidoResponseDto criar(@RequestBody PedidoRequestDto dto) {
        return pedidoService.criar(dto);
    }

    @PutMapping("/{id}")
    public PedidoResponseDto atualizar(@PathVariable Long id, @RequestBody PedidoRequestDto dto) {
        return pedidoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 