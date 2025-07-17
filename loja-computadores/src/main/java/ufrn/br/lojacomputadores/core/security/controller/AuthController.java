package ufrn.br.lojacomputadores.core.security.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import ufrn.br.lojacomputadores.core.security.dto.LoginDto;
import ufrn.br.lojacomputadores.core.security.service.TokenService;
import ufrn.br.lojacomputadores.service.AdminUserDetailsService;
import ufrn.br.lojacomputadores.service.CustomUserDetailsService;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final TokenService service;
    private final AuthenticationManager authenticationManager;
    private final AdminUserDetailsService adminUserDetailsService;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/token")
    public String getToken(@RequestBody LoginDto loginDto){
        log.info("Tentativa de login - Email: {}, Tipo: {}", loginDto.getEmail(), loginDto.getTipo());
        
        try {
            UserDetails userDetails = null;
            
            // Escolhe o UserDetailsService baseado no tipo
            if ("admin".equalsIgnoreCase(loginDto.getTipo())) {
                log.info("Tentando autenticar como ADMIN");
                userDetails = adminUserDetailsService.loadUserByUsername(loginDto.getEmail());
            } else if ("cliente".equalsIgnoreCase(loginDto.getTipo())) {
                log.info("Tentando autenticar como CLIENTE");
                userDetails = customUserDetailsService.loadUserByUsername(loginDto.getEmail());
            } else {
                throw new IllegalArgumentException("Tipo de usuário inválido. Use 'admin' ou 'cliente'");
            }
            
            // Verifica a senha
            if ("admin".equalsIgnoreCase(loginDto.getTipo())) {
                // Para admin, usa BCrypt
                log.info("Verificando senha admin - Senha fornecida: {}, Hash no banco: {}", 
                    loginDto.getSenha(), userDetails.getPassword());
                
                // Teste adicional: gerar um novo hash para comparação
                String novoHash = passwordEncoder.encode(loginDto.getSenha());
                log.info("Novo hash gerado para '{}': {}", loginDto.getSenha(), novoHash);
                log.info("Novo hash funciona? {}", passwordEncoder.matches(loginDto.getSenha(), novoHash));
                
                boolean senhaCorreta = passwordEncoder.matches(loginDto.getSenha(), userDetails.getPassword());
                log.info("Resultado da verificação BCrypt: {}", senhaCorreta);
                
                if (!senhaCorreta) {
                    throw new IllegalArgumentException("Senha incorreta");
                }
            } else {
                // Para cliente, compara diretamente com CPF (temporariamente)
                if (!loginDto.getSenha().equals(userDetails.getPassword())) {
                    throw new IllegalArgumentException("CPF incorreto");
                }
            }
            
            // Cria a autenticação
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, loginDto.getSenha(), userDetails.getAuthorities());
            
            log.info("Autenticação bem-sucedida para: {} com authorities: {}", 
                    authentication.getName(), authentication.getAuthorities());
            return service.generateToken(authentication);
        } catch (Exception e) {
            log.error("Erro na autenticação: {}", e.getMessage(), e);
            throw e;
        }
    }
}