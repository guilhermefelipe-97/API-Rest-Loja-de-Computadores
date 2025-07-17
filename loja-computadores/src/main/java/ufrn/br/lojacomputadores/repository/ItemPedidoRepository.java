package ufrn.br.lojacomputadores.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ufrn.br.lojacomputadores.domain.ItemPedido;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    
    // Buscar todos os itens de um pedido
    List<ItemPedido> findByPedidoId(Long pedidoId);
    
    // Buscar todos os itens de um pedido que não foram deletados
    List<ItemPedido> findByPedidoIdAndDeletedAtIsNull(Long pedidoId);
    
    // Buscar item por pedido e produto
    List<ItemPedido> findByPedidoIdAndProdutoIdAndDeletedAtIsNull(Long pedidoId, Long produtoId);
} 