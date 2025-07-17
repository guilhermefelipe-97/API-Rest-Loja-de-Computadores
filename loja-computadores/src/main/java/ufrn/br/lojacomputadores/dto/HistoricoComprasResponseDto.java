package ufrn.br.lojacomputadores.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HistoricoComprasResponseDto {
    private Long id;
    private Integer totalPedidos;
    private BigDecimal valorTotalGasto;
    private LocalDate primeiraCompra;
    private LocalDate ultimaCompra;
    private String categoriaPreferida;
    private String marcaPreferida;
    private String observacoes;
} 