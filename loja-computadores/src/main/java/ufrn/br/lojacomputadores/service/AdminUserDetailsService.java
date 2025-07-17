package ufrn.br.lojacomputadores.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ufrn.br.lojacomputadores.domain.Admin;
import ufrn.br.lojacomputadores.repository.AdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Service("adminUserDetailsService")
@RequiredArgsConstructor
@Slf4j
public class AdminUserDetailsService implements UserDetailsService {
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Tentando autenticar admin com email: {}", email);
        
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Admin não encontrado com email: {}", email);
                    return new UsernameNotFoundException("Admin não encontrado com email: " + email);
                });
        
        log.info("Admin encontrado: {} - Role: {}", admin.getNome(), admin.getRole());
        log.info("Senha do admin (hash): {}", admin.getSenha());
        log.info("Tamanho da senha: {}", admin.getSenha().length());
        
        // Usa o método getSenhaLimpa() que remove aspas automaticamente
        String senhaLimpa = admin.getSenhaLimpa();
        log.info("Hash limpo (sem aspas): {}", senhaLimpa);
        log.info("Tamanho do hash limpo: {}", senhaLimpa.length());
        
        // Verificar se o hash começa com $2a$ ou $2b$
        if (!senhaLimpa.startsWith("$2a$") && !senhaLimpa.startsWith("$2b$")) {
            log.error("Hash não parece ser BCrypt válido: {}", senhaLimpa);
        } else {
            log.info("Hash parece ser BCrypt válido");
        }
        
        // Criar o UserDetails
        UserDetails userDetails = new User(
                admin.getEmail(),
                senhaLimpa,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        
        log.info("UserDetails criado com sucesso para admin: {}", userDetails.getUsername());
        log.info("Authorities: {}", userDetails.getAuthorities());
        log.info("Account não expirado: {}", userDetails.isAccountNonExpired());
        log.info("Account não bloqueado: {}", userDetails.isAccountNonLocked());
        log.info("Credenciais não expiradas: {}", userDetails.isCredentialsNonExpired());
        log.info("Habilitado: {}", userDetails.isEnabled());
        
        return userDetails;
    }
} 