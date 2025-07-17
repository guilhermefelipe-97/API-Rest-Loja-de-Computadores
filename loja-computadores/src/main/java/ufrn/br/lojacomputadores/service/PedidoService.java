package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.*;
import ufrn.br.lojacomputadores.repository.ClienteRepository;
import ufrn.br.lojacomputadores.repository.PedidoRepository;
import ufrn.br.lojacomputadores.repository.ProdutoRepository;
import ufrn.br.lojacomputadores.repository.HistoricoComprasRepository;
import ufrn.br.lojacomputadores.dto.PedidoRequestDto;
import ufrn.br.lojacomputadores.dto.PedidoResponseDto;
import ufrn.br.lojacomputadores.mapper.PedidoMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class PedidoService {
    
    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final HistoricoComprasRepository historicoComprasRepository;
    private final PedidoMapper pedidoMapper;
    
    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, 
                        ProdutoRepository produtoRepository, HistoricoComprasRepository historicoComprasRepository, 
                        PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.historicoComprasRepository = historicoComprasRepository;
        this.pedidoMapper = pedidoMapper;
    }
    
    @Transactional(readOnly = true)
    public Page<Pedido> findAll(Pageable pageable) {
        log.info("Buscando todos os pedidos com paginação: {}", pageable);
        return pedidoRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Pedido> findById(Long id) {
        log.info("Buscando pedido por ID: {}", id);
        return pedidoRepository.findById(id);
    }
    
    public Pedido save(Pedido pedido) {
        log.info("Criando novo pedido para cliente ID: {}", pedido.getCliente().getId());
        
        // Validar se cliente existe
        Optional<Cliente> cliente = clienteRepository.findById(pedido.getCliente().getId());
        if (cliente.isEmpty()) {
            log.warn("Cliente não encontrado. ID: {}", pedido.getCliente().getId());
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        pedido.setCliente(cliente.get());
        pedido.setDataPedido(LocalDateTime.now());
        
        // Valor total inicial (sem itens)
        pedido.setValorTotal(BigDecimal.ZERO);
        
        Pedido savedPedido = pedidoRepository.save(pedido);
        
        // Atualizar histórico de compras do cliente
        atualizarHistoricoCompras(savedPedido);
        
        log.info("Pedido criado com sucesso. ID: {}, Valor total: {}", savedPedido.getId(), savedPedido.getValorTotal());
        return savedPedido;
    }
    
    public Pedido update(Long id, Pedido pedido) {
        log.info("Atualizando pedido ID: {}", id);
        
        Optional<Pedido> existingPedido = pedidoRepository.findById(id);
        if (existingPedido.isEmpty()) {
            log.warn("Pedido não encontrado para atualização. ID: {}", id);
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        Pedido pedidoExistente = existingPedido.get();
        
        // Apenas permitir atualização do status
        pedidoExistente.setStatus(pedido.getStatus());
        
        Pedido updatedPedido = pedidoRepository.save(pedidoExistente);
        log.info("Status do pedido atualizado com sucesso. ID: {}, Novo status: {}", 
            updatedPedido.getId(), updatedPedido.getStatus());
        return updatedPedido;
    }
    
    public void deleteById(Long id) {
        log.info("Cancelando pedido ID: {}", id);
        
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isEmpty()) {
            log.warn("Pedido não encontrado para cancelamento. ID: {}", id);
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        Pedido pedidoExistente = pedido.get();
        
        // Verificar se o pedido pode ser cancelado
        if (pedidoExistente.getStatus() == Pedido.StatusPedido.ENTREGUE) {
            log.warn("Tentativa de cancelar pedido já entregue. ID: {}", id);
            throw new IllegalArgumentException("Não é possível cancelar um pedido já entregue");
        }
        
        pedidoExistente.setStatus(Pedido.StatusPedido.CANCELADO);
        pedidoRepository.save(pedidoExistente);
        
        // Atualizar histórico de compras (remover valor do pedido cancelado)
        atualizarHistoricoComprasAposCancelamento(pedidoExistente);
        
        log.info("Pedido cancelado com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public Page<Pedido> findByClienteId(Long clienteId, Pageable pageable) {
        log.info("Buscando pedidos por cliente ID: {}", clienteId);
        return pedidoRepository.findByClienteId(clienteId, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Pedido> findByStatus(Pedido.StatusPedido status, Pageable pageable) {
        log.info("Buscando pedidos por status: {}", status);
        return pedidoRepository.findByStatus(status, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Pedido> findByDataPedidoBetween(LocalDateTime dataInicio, LocalDateTime dataFim, Pageable pageable) {
        log.info("Buscando pedidos entre {} e {}", dataInicio, dataFim);
        return pedidoRepository.findByDataPedidoBetween(dataInicio, dataFim, pageable);
    }
    
    public Pedido atualizarStatus(Long id, Pedido.StatusPedido novoStatus) {
        log.info("Atualizando status do pedido ID: {} para {}", id, novoStatus);
        
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isEmpty()) {
            log.warn("Pedido não encontrado para atualização de status. ID: {}", id);
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        Pedido pedidoExistente = pedido.get();
        pedidoExistente.setStatus(novoStatus);
        
        Pedido updatedPedido = pedidoRepository.save(pedidoExistente);
        log.info("Status do pedido atualizado com sucesso. ID: {}, Novo status: {}", 
            updatedPedido.getId(), updatedPedido.getStatus());
        return updatedPedido;
    }

    // Métodos para uso no controller REST
    public List<PedidoResponseDto> listarTodos() {
        return pedidoRepository.findAll().stream()
            .map(pedidoMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    public PedidoResponseDto buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        return pedidoMapper.toResponseDto(pedido);
    }

    public PedidoResponseDto criar(PedidoRequestDto dto) {
        log.info("Criando novo pedido para cliente ID: {}", dto.getClienteId());
        
        // Validar se cliente existe
        Optional<Cliente> cliente = clienteRepository.findById(dto.getClienteId());
        if (cliente.isEmpty()) {
            log.warn("Cliente não encontrado. ID: {}", dto.getClienteId());
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        // Criar pedido com campos obrigatórios
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente.get());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(BigDecimal.ZERO);
        pedido.setStatus(dto.getStatus() != null ? dto.getStatus() : Pedido.StatusPedido.PENDENTE);
        
        Pedido salvo = pedidoRepository.save(pedido);
        
        // Atualizar histórico de compras do cliente
        atualizarHistoricoCompras(salvo);
        
        log.info("Pedido criado com sucesso. ID: {}", salvo.getId());
        return pedidoMapper.toResponseDto(salvo);
    }

    public PedidoResponseDto atualizar(Long id, PedidoRequestDto dto) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        pedidoMapper.updateEntityFromDto(dto, pedido);
        Pedido atualizado = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDto(atualizado);
    }

    public void deletar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        pedidoRepository.deleteById(id);
    }
    
    // Método para atualizar histórico de compras quando um pedido é criado ou alterado
    private void atualizarHistoricoCompras(Pedido pedido) {
        Cliente cliente = pedido.getCliente();

        // Buscar ou criar histórico de compras
        Optional<HistoricoCompras> historicoOpt = historicoComprasRepository.findByClienteId(cliente.getId());
        HistoricoCompras historico = historicoOpt.orElseGet(() -> {
            HistoricoCompras novo = new HistoricoCompras();
            novo.setCliente(cliente);
            return novo;
        });

        // Recalcular os dados do histórico a partir dos pedidos reais
        BigDecimal valorTotal = pedidoRepository.calcularValorTotalPorCliente(cliente.getId());
        Long totalPedidos = pedidoRepository.contarPedidosPorCliente(cliente.getId());
        LocalDateTime primeiraCompra = pedidoRepository.buscarPrimeiraDataPedido(cliente.getId());
        LocalDateTime ultimaCompra = pedidoRepository.buscarUltimaDataPedido(cliente.getId());

        historico.setValorTotalGasto(valorTotal != null ? valorTotal : BigDecimal.ZERO);
        historico.setTotalPedidos(totalPedidos != null ? totalPedidos.intValue() : 0);
        historico.setPrimeiraCompra(primeiraCompra != null ? primeiraCompra.toLocalDate() : null);
        historico.setUltimaCompra(ultimaCompra != null ? ultimaCompra.toLocalDate() : null);

        historicoComprasRepository.save(historico);
        log.info("Histórico de compras recalculado para cliente ID: {}", cliente.getId());
    }

    // Método para atualizar histórico quando um pedido é cancelado
    private void atualizarHistoricoComprasAposCancelamento(Pedido pedido) {
        atualizarHistoricoCompras(pedido);
    }
}

