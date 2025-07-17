package ufrn.br.lojacomputadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufrn.br.lojacomputadores.domain.HistoricoCompras;

import java.util.Optional;

@Repository
public interface HistoricoComprasRepository extends JpaRepository<HistoricoCompras, Long> {
    Optional<HistoricoCompras> findByClienteId(Long clienteId);
} 