package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.Produto;
import ufrn.br.lojacomputadores.domain.Categoria;
import ufrn.br.lojacomputadores.repository.ProdutoRepository;
import ufrn.br.lojacomputadores.repository.CategoriaRepository;
import ufrn.br.lojacomputadores.dto.ProdutoRequestDto;
import ufrn.br.lojacomputadores.dto.ProdutoResponseDto;
import ufrn.br.lojacomputadores.mapper.ProdutoMapper;
import java.util.List;
import java.util.stream.Collectors;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.produtoMapper = produtoMapper;
    }
    
    @Transactional(readOnly = true)
    public Page<Produto> findAll(Pageable pageable) {
        log.info("Buscando todos os produtos com paginação: {}", pageable);
        return produtoRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Produto> findById(Long id) {
        log.info("Buscando produto por ID: {}", id);
        return produtoRepository.findById(id);
    }
    
    public Produto save(Produto produto) {
        log.info("Salvando produto: {}", produto.getNome());
        
        Produto savedProduto = produtoRepository.save(produto);
        log.info("Produto salvo com sucesso. ID: {}", savedProduto.getId());
        return savedProduto;
    }
    
    public Produto update(Long id, Produto produto) {
        log.info("Atualizando produto ID: {}", id);
        
        Optional<Produto> existingProduto = produtoRepository.findById(id);
        if (existingProduto.isEmpty()) {
            log.warn("Produto não encontrado para atualização. ID: {}", id);
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        produto.setId(id);
        Produto updatedProduto = produtoRepository.save(produto);
        log.info("Produto atualizado com sucesso. ID: {}", updatedProduto.getId());
        return updatedProduto;
    }
    
    public void deleteById(Long id) {
        log.info("Deletando produto ID: {}", id);
        
        if (!produtoRepository.existsById(id)) {
            log.warn("Produto não encontrado para deleção. ID: {}", id);
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        produtoRepository.deleteById(id);
        log.info("Produto deletado com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public Page<Produto> findByNomeContaining(String nome, Pageable pageable) {
        log.info("Buscando produtos por nome contendo: {}", nome);
        return produtoRepository.findByNomeContaining(nome, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Produto> findByPrecoRange(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable) {
        log.info("Buscando produtos por faixa de preço: {} - {}", precoMin, precoMax);
        return produtoRepository.findByPrecoRange(precoMin, precoMax, pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Produto> findProdutosEmEstoque(Pageable pageable) {
        log.info("Buscando produtos em estoque");
        return produtoRepository.findProdutosEmEstoque(pageable);
    }
    
    @Transactional(readOnly = true)
    public Page<Produto> findProdutosComEstoqueBaixo(Integer limite, Pageable pageable) {
        log.info("Buscando produtos com estoque baixo (limite: {})", limite);
        return produtoRepository.findProdutosComEstoqueBaixo(limite, pageable);
    }
    
    public void atualizarEstoque(Long produtoId, Integer novaQuantidade) {
        log.info("Atualizando estoque do produto ID: {} para quantidade: {}", produtoId, novaQuantidade);
        
        Optional<Produto> produto = produtoRepository.findById(produtoId);
        if (produto.isEmpty()) {
            log.warn("Produto não encontrado para atualização de estoque. ID: {}", produtoId);
            throw new IllegalArgumentException("Produto não encontrado");
        }
        
        Produto produtoAtualizado = produto.get();
        produtoAtualizado.setQuantidadeEstoque(novaQuantidade);
        produtoRepository.save(produtoAtualizado);
        
        log.info("Estoque atualizado com sucesso. Produto ID: {}, Nova quantidade: {}", produtoId, novaQuantidade);
    }

    // Métodos para uso no controller REST
    public List<ProdutoResponseDto> listarTodos() {
        return produtoRepository.findAll().stream()
            .map(this::mapToResponseDtoWithCategorias)
            .collect(Collectors.toList());
    }

    public ProdutoResponseDto buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        return mapToResponseDtoWithCategorias(produto);
    }

    public ProdutoResponseDto criar(ProdutoRequestDto dto) {
        Produto produto = produtoMapper.toEntity(dto);
        
        // Associar categorias se fornecidas
        if (dto.getCategoriaIds() != null && !dto.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
            produto.setCategorias(categorias);
        }
        
        Produto salvo = produtoRepository.save(produto);
        return mapToResponseDtoWithCategorias(salvo);
    }

    public ProdutoResponseDto atualizar(Long id, ProdutoRequestDto dto) {
        Produto produto = produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        
        produtoMapper.updateEntityFromDto(dto, produto);
        
        // Atualizar categorias se fornecidas
        if (dto.getCategoriaIds() != null) {
            List<Categoria> categorias = categoriaRepository.findAllById(dto.getCategoriaIds());
            produto.setCategorias(categorias);
        }
        
        Produto atualizado = produtoRepository.save(produto);
        return mapToResponseDtoWithCategorias(atualizado);
    }

    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }
    
    // Método auxiliar para mapear produto com IDs das categorias
    private ProdutoResponseDto mapToResponseDtoWithCategorias(Produto produto) {
        ProdutoResponseDto dto = produtoMapper.toResponseDto(produto);
        
        if (produto.getCategorias() != null) {
            List<Long> categoriaIds = produto.getCategorias().stream()
                .map(categoria -> categoria.getId())
                .collect(Collectors.toList());
            dto.setCategoriaIds(categoriaIds);
        }
        
        return dto;
    }
}

