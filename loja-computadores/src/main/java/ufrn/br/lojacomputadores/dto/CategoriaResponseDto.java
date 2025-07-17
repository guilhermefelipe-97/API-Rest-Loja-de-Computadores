package ufrn.br.lojacomputadores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponseDto {
    
    private Long id;
    private String nome;
    private String descricao;
    private List<ProdutoResponseDto> produtos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 