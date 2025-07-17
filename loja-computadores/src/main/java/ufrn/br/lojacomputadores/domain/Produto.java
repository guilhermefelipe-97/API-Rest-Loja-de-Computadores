package ufrn.br.lojacomputadores.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import ufrn.br.lojacomputadores.core.base.BaseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE produto SET deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at is null")
public class Produto extends BaseEntity {
    
    @NotBlank(message = "Nome do produto é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;
    
    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Column(nullable = false)
    private Integer quantidadeEstoque;
    
    private String especificacoesTecnicas;
    
    // Relacionamento N-N: Um produto pode pertencer a muitas categorias
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "produto_categoria",
        joinColumns = @JoinColumn(name = "produto_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    @ToString.Exclude
    private List<Categoria> categorias = new ArrayList<>();
}

