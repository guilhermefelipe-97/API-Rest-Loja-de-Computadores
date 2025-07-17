package ufrn.br.lojacomputadores.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ufrn.br.lojacomputadores.domain.Cliente;
import ufrn.br.lojacomputadores.repository.ClienteRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Tentando autenticar cliente com email: {}", email);
        
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Cliente não encontrado com email: {}", email);
                    return new UsernameNotFoundException("Cliente não encontrado com email: " + email);
                });
        
        log.info("Cliente encontrado: {} - Email: {}", cliente.getNome(), cliente.getEmail());
        
        // Aqui, para simplificar, todos os clientes terão o papel USER
        // Usando CPF como senha temporariamente (você pode adicionar um campo senha depois)
        return new User(
                cliente.getEmail(),
                cliente.getCpf(), // Usando CPF como senha temporariamente
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
} 