package ufrn.br.lojacomputadores;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ufrn.br.lojacomputadores.core.security.RsaKeyProperties;

@SpringBootApplication
@EnableConfigurationProperties (RsaKeyProperties.class)
public class LojaComputadoresApplication {

    public static void main(String[] args) {
        SpringApplication.run(LojaComputadoresApplication.class, args);
    }
}
