package ufrn.br.lojacomputadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufrn.br.lojacomputadores.domain.Categoria;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
    // Buscar categoria por nome
    Optional<Categoria> findByNomeAndDeletedAtIsNull(String nome);
    
    // Verificar se categoria existe por nome
    boolean existsByNomeAndDeletedAtIsNull(String nome);
} 