package ufrn.br.lojacomputadores.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import ufrn.br.lojacomputadores.domain.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponseDto extends RepresentationModel<PedidoResponseDto> {
    
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private Pedido.StatusPedido status;
    private ClienteResponseDto cliente;
    private List<ItemPedidoResponseDto> itens;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

