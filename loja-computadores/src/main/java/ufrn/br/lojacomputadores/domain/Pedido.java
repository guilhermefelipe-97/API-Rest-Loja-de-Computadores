package ufrn.br.lojacomputadores.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import ufrn.br.lojacomputadores.domain.Cliente;
import ufrn.br.lojacomputadores.core.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE pedido SET deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at is null")
public class Pedido extends BaseEntity {
    
    @NotNull(message = "Data do pedido é obrigatória")
    @Column(nullable = false)
    private LocalDateTime dataPedido;
    
    @NotNull(message = "Valor total é obrigatório")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status;
    
    // Relacionamento N-1: Muitos pedidos pertencem a um cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @ToString.Exclude
    private Cliente cliente;
    
    // Relacionamento 1-N: Um pedido pode ter muitos itens
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @ToString.Exclude
    private List<ItemPedido> itens = new ArrayList<>();
    
    public enum StatusPedido {
        PENDENTE,
        PROCESSANDO,
        ENVIADO,
        ENTREGUE,
        CANCELADO
    }
    
    // Método para calcular o valor total do pedido
    public void calcularValorTotal() {
        this.valorTotal = itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Método para adicionar item ao pedido
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
        calcularValorTotal();
    }
    
    // Método para remover item do pedido
    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
        calcularValorTotal();
    }
}

