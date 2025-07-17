package ufrn.br.lojacomputadores.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaRequestDto {
    
    @NotBlank(message = "Nome da categoria é obrigatório")
    private String nome;
    
    private String descricao;
} 