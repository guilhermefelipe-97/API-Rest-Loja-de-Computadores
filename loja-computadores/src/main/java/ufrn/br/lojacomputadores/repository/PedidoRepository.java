package ufrn.br.lojacomputadores.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ufrn.br.lojacomputadores.domain.Pedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByClienteId(Long clienteId, Pageable pageable);
    Page<Pedido> findByStatus(Pedido.StatusPedido status, Pageable pageable);
    Page<Pedido> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable);

    // Calcular valor total dos pedidos de um cliente (apenas pedidos não cancelados)
    @Query("SELECT COALESCE(SUM(p.valorTotal), 0) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status <> 'CANCELADO'")
    BigDecimal calcularValorTotalPorCliente(@Param("clienteId") Long clienteId);

    // Contar total de pedidos de um cliente (apenas pedidos não cancelados)
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status <> 'CANCELADO'")
    Long contarPedidosPorCliente(@Param("clienteId") Long clienteId);

    // Buscar primeira data de pedido de um cliente
    @Query("SELECT MIN(p.dataPedido) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status <> 'CANCELADO'")
    LocalDateTime buscarPrimeiraDataPedido(@Param("clienteId") Long clienteId);

    // Buscar última data de pedido de um cliente
    @Query("SELECT MAX(p.dataPedido) FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status <> 'CANCELADO'")
    LocalDateTime buscarUltimaDataPedido(@Param("clienteId") Long clienteId);
} 