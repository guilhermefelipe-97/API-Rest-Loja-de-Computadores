package ufrn.br.lojacomputadores.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ufrn.br.lojacomputadores.domain.HistoricoCompras;
import ufrn.br.lojacomputadores.repository.HistoricoComprasRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoricoComprasService {
    private final HistoricoComprasRepository historicoComprasRepository;

    @Transactional(readOnly = true)
    public Optional<HistoricoCompras> findById(Long id) {
        return historicoComprasRepository.findById(id);
    }

    public HistoricoCompras save(HistoricoCompras historicoCompras) {
        return historicoComprasRepository.save(historicoCompras);
    }

    public void deleteById(Long id) {
        historicoComprasRepository.deleteById(id);
    }
} 