package ufrn.br.lojacomputadores.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import ufrn.br.lojacomputadores.core.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE categoria SET deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at is null")
public class Categoria extends BaseEntity {
    
    @NotBlank(message = "Nome da categoria é obrigatório")
    @Column(nullable = false, unique = true)
    private String nome;
    
    private String descricao;
    
    // Relacionamento N-N: Uma categoria pode ter muitos produtos
    @ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Produto> produtos = new ArrayList<>();
} 