package ufrn.br.lojacomputadores.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufrn.br.lojacomputadores.domain.Produto;

import java.math.BigDecimal;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    @Query("SELECT p FROM Produto p WHERE p.nome LIKE %:nome%")
    Page<Produto> findByNomeContaining(@Param("nome") String nome, Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax")
    Page<Produto> findByPrecoRange(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax, Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque > 0")
    Page<Produto> findProdutosEmEstoque(Pageable pageable);
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque < :limite")
    Page<Produto> findProdutosComEstoqueBaixo(@Param("limite") Integer limite, Pageable pageable);
}

