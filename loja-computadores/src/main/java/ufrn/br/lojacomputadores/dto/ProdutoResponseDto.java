package ufrn.br.lojacomputadores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponseDto extends RepresentationModel<ProdutoResponseDto> {
    
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private String especificacoesTecnicas;
    private List<Long> categoriaIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

