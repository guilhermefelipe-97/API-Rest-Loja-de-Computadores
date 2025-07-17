package ufrn.br.lojacomputadores.core.security.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String senha;
    private String tipo; // "admin" ou "cliente"
}