package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.dto.ItemPedidoRequestDto;
import ufrn.br.lojacomputadores.dto.ItemPedidoResponseDto;
import ufrn.br.lojacomputadores.service.ItemPedidoService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos/{pedidoId}/itens")
@RequiredArgsConstructor
public class ItemPedidoController {
    
    private final ItemPedidoService itemPedidoService;
    
    @PostMapping
    public ResponseEntity<ItemPedidoResponseDto> adicionarItem(
            @PathVariable Long pedidoId,
            @Valid @RequestBody ItemPedidoRequestDto dto) {
        ItemPedidoResponseDto item = itemPedidoService.adicionarItem(pedidoId, dto);
        return ResponseEntity.ok(item);
    }
    
    @GetMapping
    public ResponseEntity<List<ItemPedidoResponseDto>> listarItens(@PathVariable Long pedidoId) {
        List<ItemPedidoResponseDto> itens = itemPedidoService.listarItensDoPedido(pedidoId);
        return ResponseEntity.ok(itens);
    }
    
    @PutMapping("/{itemId}/quantidade")
    public ResponseEntity<ItemPedidoResponseDto> atualizarQuantidade(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId,
            @RequestParam Integer quantidade) {
        ItemPedidoResponseDto item = itemPedidoService.atualizarQuantidade(pedidoId, itemId, quantidade);
        return ResponseEntity.ok(item);
    }
    
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> removerItem(
            @PathVariable Long pedidoId,
            @PathVariable Long itemId) {
        itemPedidoService.removerItem(pedidoId, itemId);
        return ResponseEntity.noContent().build();
    }
} 