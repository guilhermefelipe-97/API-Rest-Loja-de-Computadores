package ufrn.br.lojacomputadores.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import ufrn.br.lojacomputadores.core.base.BaseEntity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE admin SET deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at is null")
public class Admin extends BaseEntity {
    @NotBlank(message = "Nome do admin é obrigatório")
    @Column(nullable = false)
    private String nome;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role = "ADMIN";
    
    /**
     * Retorna a senha limpa (sem aspas duplas)
     */
    @Transient
    public String getSenhaLimpa() {
        if (senha != null && senha.startsWith("\"") && senha.endsWith("\"")) {
            return senha.substring(1, senha.length() - 1);
        }
        return senha;
    }
} 