package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.ItemPedido;
import ufrn.br.lojacomputadores.domain.Pedido;
import ufrn.br.lojacomputadores.domain.Produto;
import ufrn.br.lojacomputadores.dto.ItemPedidoRequestDto;
import ufrn.br.lojacomputadores.dto.ItemPedidoResponseDto;
import ufrn.br.lojacomputadores.mapper.ItemPedidoMapper;
import ufrn.br.lojacomputadores.repository.ItemPedidoRepository;
import ufrn.br.lojacomputadores.repository.PedidoRepository;
import ufrn.br.lojacomputadores.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ItemPedidoService {
    
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoMapper itemPedidoMapper;
    
    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository, 
                           PedidoRepository pedidoRepository, 
                           ProdutoRepository produtoRepository, 
                           ItemPedidoMapper itemPedidoMapper) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoMapper = itemPedidoMapper;
    }
    
    public ItemPedidoResponseDto adicionarItem(Long pedidoId, ItemPedidoRequestDto dto) {
        log.info("Adicionando item ao pedido ID: {}, Produto ID: {}, Quantidade: {}", 
                pedidoId, dto.getProdutoId(), dto.getQuantidade());
        
        // Buscar pedido
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);
        if (pedidoOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        // Buscar produto
        Optional<Produto> produtoOpt = produtoRepository.findById(dto.getProdutoId());
        if (produtoOpt.isEmpty()) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        Pedido pedido = pedidoOpt.get();
        Produto produto = produtoOpt.get();
        
        // Verificar se o item já existe no pedido
        Optional<ItemPedido> itemExistente = itemPedidoRepository.findByPedidoIdAndProdutoIdAndDeletedAtIsNull(pedidoId, dto.getProdutoId())
                .stream()
                .findFirst();
        
        ItemPedido itemPedido;
        
        if (itemExistente.isPresent()) {
            // Atualizar quantidade do item existente
            itemPedido = itemExistente.get();
            itemPedido.setQuantidade(itemPedido.getQuantidade() + dto.getQuantidade());
            log.info("Atualizando quantidade do item existente. Nova quantidade: {}", itemPedido.getQuantidade());
        } else {
            // Criar novo item
            itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            log.info("Criando novo item para o pedido");
        }
        
        // Calcular subtotal
        itemPedido.calcularSubtotal();
        
        // Salvar item
        ItemPedido itemSalvo = itemPedidoRepository.save(itemPedido);
        
        // Recalcular valor total do pedido
        pedido.calcularValorTotal();
        pedidoRepository.save(pedido);
        
        log.info("Item adicionado com sucesso. Subtotal: {}", itemSalvo.getSubtotal());
        return itemPedidoMapper.toResponseDto(itemSalvo);
    }
    
    public void removerItem(Long pedidoId, Long itemId) {
        log.info("Removendo item ID: {} do pedido ID: {}", itemId, pedidoId);
        
        Optional<ItemPedido> itemOpt = itemPedidoRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item não encontrado");
        }
        
        ItemPedido item = itemOpt.get();
        if (!item.getPedido().getId().equals(pedidoId)) {
            throw new IllegalArgumentException("Item não pertence ao pedido");
        }
        
        // Remover item
        itemPedidoRepository.delete(item);
        
        // Recalcular valor total do pedido
        Pedido pedido = item.getPedido();
        pedido.calcularValorTotal();
        pedidoRepository.save(pedido);
        
        log.info("Item removido com sucesso");
    }
    
    public List<ItemPedidoResponseDto> listarItensDoPedido(Long pedidoId) {
        log.info("Listando itens do pedido ID: {}", pedidoId);
        
        List<ItemPedido> itens = itemPedidoRepository.findByPedidoIdAndDeletedAtIsNull(pedidoId);
        
        return itens.stream()
                .map(itemPedidoMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    public ItemPedidoResponseDto atualizarQuantidade(Long pedidoId, Long itemId, Integer novaQuantidade) {
        log.info("Atualizando quantidade do item ID: {} para {}", itemId, novaQuantidade);
        
        Optional<ItemPedido> itemOpt = itemPedidoRepository.findById(itemId);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("Item não encontrado");
        }
        
        ItemPedido item = itemOpt.get();
        if (!item.getPedido().getId().equals(pedidoId)) {
            throw new IllegalArgumentException("Item não pertence ao pedido");
        }
        
        if (novaQuantidade <= 0) {
            // Se quantidade for zero ou negativa, remover o item
            removerItem(pedidoId, itemId);
            return null;
        }
        
        // Atualizar quantidade
        item.setQuantidade(novaQuantidade);
        item.calcularSubtotal();
        
        ItemPedido itemAtualizado = itemPedidoRepository.save(item);
        
        // Recalcular valor total do pedido
        Pedido pedido = item.getPedido();
        pedido.calcularValorTotal();
        pedidoRepository.save(pedido);
        
        log.info("Quantidade atualizada com sucesso. Novo subtotal: {}", itemAtualizado.getSubtotal());
        return itemPedidoMapper.toResponseDto(itemAtualizado);
    }
} 