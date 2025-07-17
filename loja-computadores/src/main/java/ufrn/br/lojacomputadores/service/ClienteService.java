package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.Cliente;
import ufrn.br.lojacomputadores.repository.ClienteRepository;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ClienteService {
    private final ClienteRepository clienteRepository;
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        log.info("Buscando todos os clientes com paginação: {}", pageable);
        return clienteRepository.findAll(pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findById(Long id) {
        log.info("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id);
    }
    
    public Cliente save(Cliente cliente) {
        log.info("Salvando cliente: {}", cliente.getNome());
        
        // Validar se email já existe
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            log.warn("Tentativa de criar cliente com email já existente: {}", cliente.getEmail());
            throw new IllegalArgumentException("Já existe um cliente com este email");
        }
        
        // Validar se CPF já existe
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            log.warn("Tentativa de criar cliente com CPF já existente: {}", cliente.getCpf());
            throw new IllegalArgumentException("Já existe um cliente com este CPF");
        }
        
        Cliente savedCliente = clienteRepository.save(cliente);
        log.info("Cliente salvo com sucesso. ID: {}", savedCliente.getId());
        return savedCliente;
    }
    
    public Cliente update(Long id, Cliente cliente) {
        log.info("Atualizando cliente ID: {}", id);
        
        Optional<Cliente> existingCliente = clienteRepository.findById(id);
        if (existingCliente.isEmpty()) {
            log.warn("Cliente não encontrado para atualização. ID: {}", id);
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        Cliente clienteExistente = existingCliente.get();
        
        // Verificar se o email já existe em outro cliente
        Optional<Cliente> clienteComMesmoEmail = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteComMesmoEmail.isPresent() && !clienteComMesmoEmail.get().getId().equals(id)) {
            log.warn("Tentativa de atualizar cliente com email já existente: {}", cliente.getEmail());
            throw new IllegalArgumentException("Já existe um cliente com este email");
        }
        
        // Verificar se o CPF já existe em outro cliente
        Optional<Cliente> clienteComMesmoCpf = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteComMesmoCpf.isPresent() && !clienteComMesmoCpf.get().getId().equals(id)) {
            log.warn("Tentativa de atualizar cliente com CPF já existente: {}", cliente.getCpf());
            throw new IllegalArgumentException("Já existe um cliente com este CPF");
        }
        
        // Atualizar apenas os campos permitidos, preservando createdAt
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setCpf(cliente.getCpf());
        clienteExistente.setTelefone(cliente.getTelefone());
        
        Cliente updatedCliente = clienteRepository.save(clienteExistente);
        log.info("Cliente atualizado com sucesso. ID: {}", updatedCliente.getId());
        return updatedCliente;
    }
    
    public void deleteById(Long id) {
        log.info("Deletando cliente ID: {}", id);
        
        if (!clienteRepository.existsById(id)) {
            log.warn("Cliente não encontrado para deleção. ID: {}", id);
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        clienteRepository.deleteById(id);
        log.info("Cliente deletado com sucesso. ID: {}", id);
    }
    
    @Transactional(readOnly = true)
    public Page<Cliente> findByNomeContaining(String nome, Pageable pageable) {
        log.info("Buscando clientes por nome contendo: {}", nome);
        return clienteRepository.findByNomeContaining(nome, pageable);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByEmail(String email) {
        log.info("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }
    
    @Transactional(readOnly = true)
    public Optional<Cliente> findByCpf(String cpf) {
        log.info("Buscando cliente por CPF: {}", cpf);
        return clienteRepository.findByCpf(cpf);
    }
}

