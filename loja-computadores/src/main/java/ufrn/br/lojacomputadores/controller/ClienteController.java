package ufrn.br.lojacomputadores.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ufrn.br.lojacomputadores.domain.Cliente;
import ufrn.br.lojacomputadores.dto.ClienteRequestDto;
import ufrn.br.lojacomputadores.dto.ClienteResponseDto;
import ufrn.br.lojacomputadores.mapper.ClienteMapper;
import ufrn.br.lojacomputadores.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    @GetMapping
    public Page<ClienteResponseDto> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return clienteService.findAll(pageable).map(clienteMapper::toResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> buscarPorId(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(clienteMapper::toResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> criar(@Valid @RequestBody ClienteRequestDto dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente salvo = clienteService.save(cliente);
        return ResponseEntity.ok(clienteMapper.toResponseDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDto dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente atualizado = clienteService.update(id, cliente);
        return ResponseEntity.ok(clienteMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 