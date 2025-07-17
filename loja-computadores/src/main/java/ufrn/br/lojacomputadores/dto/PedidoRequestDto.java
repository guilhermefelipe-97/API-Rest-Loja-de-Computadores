package ufrn.br.lojacomputadores.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ufrn.br.lojacomputadores.domain.Pedido;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDto {
    
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
    
    private Pedido.StatusPedido status = Pedido.StatusPedido.PENDENTE;
}

