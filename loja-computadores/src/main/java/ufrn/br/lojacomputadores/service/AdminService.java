package ufrn.br.lojacomputadores.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.Admin;
import ufrn.br.lojacomputadores.repository.AdminRepository;

import java.util.Optional;

@Service
@Slf4j
@Transactional
public class AdminService {
    private final AdminRepository adminRepository;
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional(readOnly = true)
    public Page<Admin> findAll(Pageable pageable) {
        return adminRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin save(Admin admin) {
        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new IllegalArgumentException("Já existe um admin com este email");
        }
        return adminRepository.save(admin);
    }

    public Admin update(Long id, Admin admin) {
        Optional<Admin> existing = adminRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Admin não encontrado");
        }
        admin.setId(id);
        return adminRepository.save(admin);
    }

    public void deleteById(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new IllegalArgumentException("Admin não encontrado");
        }
        adminRepository.deleteById(id);
    }
} 