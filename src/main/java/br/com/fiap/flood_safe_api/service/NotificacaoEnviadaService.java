package br.com.fiap.flood_safe_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.NotificacaoRequest;
import br.com.fiap.flood_safe_api.dto.NotificacaoResponse;
import br.com.fiap.flood_safe_api.dto.NotificacaoStatusUpdateRequest;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.model.Morador;
import br.com.fiap.flood_safe_api.model.NotificacaoEnviada;
import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;
import br.com.fiap.flood_safe_api.repository.AlertaRepository;
import br.com.fiap.flood_safe_api.repository.MoradorRepository;
import br.com.fiap.flood_safe_api.repository.NotificacaoEnviadaRepository;

@Service
public class NotificacaoEnviadaService {

    private final NotificacaoEnviadaRepository notificacaoRepository;
    private final AlertaRepository alertaRepository;
    private final MoradorRepository moradorRepository;

    @Autowired
    public NotificacaoEnviadaService(NotificacaoEnviadaRepository notificacaoRepository,
            AlertaRepository alertaRepository, MoradorRepository moradorRepository) {
        this.notificacaoRepository = notificacaoRepository;
        this.alertaRepository = alertaRepository;
        this.moradorRepository = moradorRepository;
    }

    @Transactional(readOnly = true)
    public Page<NotificacaoResponse> findAll(Specification<NotificacaoEnviada> spec, Pageable pageable) {
        return notificacaoRepository.findAll(spec, pageable).map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public NotificacaoResponse findById(Long id) {
        NotificacaoEnviada notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificação com ID " + id + " não encontrada."));
        return convertToResponseDto(notificacao);
    }

    @Transactional
    public NotificacaoResponse create(NotificacaoRequest requestDto) {
        Alerta alerta = alertaRepository.findById(requestDto.alertaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Alerta com ID " + requestDto.alertaId() + " não encontrado."));

        Morador morador = moradorRepository.findById(requestDto.moradorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Morador com ID " + requestDto.moradorId() + " não encontrado."));

        NotificacaoEnviada notificacao = convertToEntity(requestDto, alerta, morador);
        notificacao.setDataHoraEnvio(LocalDateTime.now());
        notificacao.setStatusEntrega(StatusEntrega.PENDENTE);

        NotificacaoEnviada savedNotificacao = notificacaoRepository.save(notificacao);
        return convertToResponseDto(savedNotificacao);
    }

    @Transactional
    public NotificacaoResponse updateStatus(Long id, NotificacaoStatusUpdateRequest requestDto) {
        NotificacaoEnviada notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notificação com ID " + id + " não encontrada para atualização de status."));

        notificacao.setStatusEntrega(requestDto.statusEntrega());

        NotificacaoEnviada updatedNotificacao = notificacaoRepository.save(notificacao);
        return convertToResponseDto(updatedNotificacao);
    }

    @Transactional
    public void delete(Long id) {
        if (!notificacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notificação com ID " + id + " não encontrada para exclusão.");
        }
        notificacaoRepository.deleteById(id);
    }

    private NotificacaoResponse convertToResponseDto(NotificacaoEnviada notificacao) {
        return new NotificacaoResponse(
                notificacao.getIdNotificacao(),
                notificacao.getAlerta().getIdAlerta(),
                notificacao.getMorador().getIdMorador(),
                notificacao.getDataHoraEnvio(),
                notificacao.getStatusEntrega());
    }

    private NotificacaoEnviada convertToEntity(NotificacaoRequest requestDto, Alerta alerta, Morador morador) {
        return NotificacaoEnviada.builder()
                .alerta(alerta)
                .morador(morador)
                .build();
    }
}
