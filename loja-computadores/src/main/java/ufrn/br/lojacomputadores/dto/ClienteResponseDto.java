package ufrn.br.lojacomputadores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDto extends RepresentationModel<ClienteResponseDto> {
    
    private Long id;
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
    private HistoricoComprasResponseDto historicoCompras;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

