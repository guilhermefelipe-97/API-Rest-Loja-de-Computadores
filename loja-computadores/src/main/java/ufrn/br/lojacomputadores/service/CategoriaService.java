package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.Categoria;
import ufrn.br.lojacomputadores.dto.CategoriaRequestDto;
import ufrn.br.lojacomputadores.dto.CategoriaResponseDto;
import ufrn.br.lojacomputadores.mapper.CategoriaMapper;
import ufrn.br.lojacomputadores.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CategoriaService {
    
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    
    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }
    
    public CategoriaResponseDto criar(CategoriaRequestDto dto) {
        log.info("Criando categoria: {}", dto.getNome());
        
        // Verificar se categoria já existe
        if (categoriaRepository.existsByNomeAndDeletedAtIsNull(dto.getNome())) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }
        
        Categoria categoria = categoriaMapper.toEntity(dto);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        
        log.info("Categoria criada com sucesso. ID: {}", categoriaSalva.getId());
        return categoriaMapper.toResponseDto(categoriaSalva);
    }
    
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listarTodos() {
        log.info("Listando todas as categorias");
        
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CategoriaResponseDto buscarPorId(Long id) {
        log.info("Buscando categoria por ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        return categoriaMapper.toResponseDto(categoria);
    }
    
    public CategoriaResponseDto atualizar(Long id, CategoriaRequestDto dto) {
        log.info("Atualizando categoria ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        // Verificar se o novo nome já existe em outra categoria
        Optional<Categoria> categoriaComMesmoNome = categoriaRepository.findByNomeAndDeletedAtIsNull(dto.getNome());
        if (categoriaComMesmoNome.isPresent() && !categoriaComMesmoNome.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe uma categoria com este nome");
        }
        
        categoriaMapper.updateEntityFromDto(dto, categoria);
        Categoria categoriaAtualizada = categoriaRepository.save(categoria);
        
        log.info("Categoria atualizada com sucesso. ID: {}", categoriaAtualizada.getId());
        return categoriaMapper.toResponseDto(categoriaAtualizada);
    }
    
    public void deletar(Long id) {
        log.info("Deletando categoria ID: {}", id);
        
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("Categoria não encontrada");
        }
        
        categoriaRepository.deleteById(id);
        log.info("Categoria deletada com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public CategoriaResponseDto buscarPorNome(String nome) {
        log.info("Buscando categoria por nome: {}", nome);
        
        Categoria categoria = categoriaRepository.findByNomeAndDeletedAtIsNull(nome)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        
        return categoriaMapper.toResponseDto(categoria);
    }
} 