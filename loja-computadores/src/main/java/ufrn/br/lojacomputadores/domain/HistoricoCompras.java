package ufrn.br.lojacomputadores.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import ufrn.br.lojacomputadores.core.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE historico_compras SET deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at is null")
public class HistoricoCompras extends BaseEntity {
    
    @NotNull(message = "Total de pedidos é obrigatório")
    @Column(nullable = false)
    private Integer totalPedidos;
    
    @NotNull(message = "Valor total gasto é obrigatório")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalGasto;
    
    @Column(nullable = false)
    private LocalDate primeiraCompra;
    
    @Column(nullable = false)
    private LocalDate ultimaCompra;
    
    @Column(length = 100)
    private String categoriaPreferida;
    
    @Column(length = 50)
    private String marcaPreferida;
    
    @Column(length = 200)
    private String observacoes;
    
    // Relacionamento11 histórico de compras pertence a um cliente
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    @ToString.Exclude
    private Cliente cliente;
} 