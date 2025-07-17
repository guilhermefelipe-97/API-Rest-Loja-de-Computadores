package ufrn.br.lojacomputadores.core.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ufrn.br.lojacomputadores.core.security.RsaKeyProperties;
import ufrn.br.lojacomputadores.service.AdminUserDetailsService;
import ufrn.br.lojacomputadores.service.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfig {

    private final RsaKeyProperties rsaKeys;
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger/",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/api/public/authenticate",
            "/actuator/*",
            "/swagger-ui/**",
            "/api-docs/swagger-config/",
            "/token",
            "/credenciais",
            "/api-docs",
            "/api-docs/**"
    };

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService customUserDetailsService, 
                                                      AdminUserDetailsService adminUserDetailsService, 
                                                      BCryptPasswordEncoder encoder){
        log.info("Configurando AuthenticationManager...");
        
        // Provider para Admin
        var adminProvider = new DaoAuthenticationProvider();
        adminProvider.setUserDetailsService(adminUserDetailsService);
        adminProvider.setPasswordEncoder(encoder);
        adminProvider.setHideUserNotFoundExceptions(false);
        log.info("Provider de Admin configurado com AdminUserDetailsService");
        
        // Provider para Cliente
        var clienteProvider = new DaoAuthenticationProvider();
        clienteProvider.setUserDetailsService(customUserDetailsService);
        clienteProvider.setPasswordEncoder(encoder);
        clienteProvider.setHideUserNotFoundExceptions(false);
        log.info("Provider de Cliente configurado com CustomUserDetailsService");
        
        // Retorna um ProviderManager que tenta apenas o primeiro provider que funcionar
        ProviderManager providerManager = new ProviderManager(adminProvider, clienteProvider);
        log.info("ProviderManager criado com {} providers", providerManager.getProviders().size());
        
        return providerManager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyRequest().authenticated();
                } )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder encoder(){
        // Configurar BCrypt com força 10 (padrão)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        log.info("BCryptPasswordEncoder configurado com força 10");
        return encoder;
    }

}