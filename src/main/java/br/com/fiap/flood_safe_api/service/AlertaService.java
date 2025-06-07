package br.com.fiap.flood_safe_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.AlertaRequest;
import br.com.fiap.flood_safe_api.dto.AlertaResponse;
import br.com.fiap.flood_safe_api.exception.BusinessException;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.LeituraSensor;
import br.com.fiap.flood_safe_api.repository.AlertaRepository;
import br.com.fiap.flood_safe_api.repository.ComunidadeRepository;
import br.com.fiap.flood_safe_api.repository.LeituraSensorRepository;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final LeituraSensorRepository leituraSensorRepository;
    private final ComunidadeRepository comunidadeRepository;

    @Autowired
    public AlertaService(AlertaRepository alertaRepository, LeituraSensorRepository leituraSensorRepository,
            ComunidadeRepository comunidadeRepository) {
        this.alertaRepository = alertaRepository;
        this.leituraSensorRepository = leituraSensorRepository;
        this.comunidadeRepository = comunidadeRepository;
    }

    @Transactional(readOnly = true)
    public Page<AlertaResponse> findAll(Specification<Alerta> spec, Pageable pageable) {
        return alertaRepository.findAll(spec, pageable).map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public AlertaResponse findById(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta com ID " + id + " não encontrado."));
        return convertToResponseDto(alerta);
    }

    @Transactional
    public AlertaResponse create(AlertaRequest requestDto) {
        LeituraSensor leituraGatilho = leituraSensorRepository.findById(requestDto.leituraGatilhoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leitura gatilho com ID " + requestDto.leituraGatilhoId() + " não encontrada."));

        Comunidade comunidadeAfetada = comunidadeRepository.findById(requestDto.comunidadeAfetadaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade afetada com ID " + requestDto.comunidadeAfetadaId() + " não encontrada."));

        // Regra de negócio: uma leitura só pode gerar um único alerta
        if (alertaRepository.findByLeituraGatilho(leituraGatilho).isPresent()) {
            throw new BusinessException(
                    "Já existe um alerta para a leitura gatilho com ID " + requestDto.leituraGatilhoId());
        }

        Alerta alerta = convertToEntity(requestDto, leituraGatilho, comunidadeAfetada);
        alerta.setDataHoraAlerta(LocalDateTime.now());

        Alerta savedAlerta = alertaRepository.save(alerta);
        return convertToResponseDto(savedAlerta);
    }

    @Transactional
    public void delete(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta com ID " + id + " não encontrado para exclusão.");
        }
        alertaRepository.deleteById(id);
    }

    private AlertaResponse convertToResponseDto(Alerta alerta) {
        return new AlertaResponse(
                alerta.getIdAlerta(),
                alerta.getLeituraGatilho().getIdLeitura(),
                alerta.getComunidadeAfetada().getIdComunidade(),
                alerta.getComunidadeAfetada().getNomeComunidade(),
                alerta.getDataHoraAlerta(),
                alerta.getTipoAlerta(),
                alerta.getMensagemAlerta());
    }

    private Alerta convertToEntity(AlertaRequest requestDto, LeituraSensor leituraGatilho,
            Comunidade comunidadeAfetada) {
        return Alerta.builder()
                .leituraGatilho(leituraGatilho)
                .comunidadeAfetada(comunidadeAfetada)
                .tipoAlerta(requestDto.tipoAlerta())
                .mensagemAlerta(requestDto.mensagemAlerta())
                .build();
    }
}
