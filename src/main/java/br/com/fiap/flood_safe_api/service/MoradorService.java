package br.com.fiap.flood_safe_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.MoradorRequest;
import br.com.fiap.flood_safe_api.dto.MoradorResponse;
import br.com.fiap.flood_safe_api.exception.BusinessException;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.Morador;
import br.com.fiap.flood_safe_api.repository.ComunidadeRepository;
import br.com.fiap.flood_safe_api.repository.MoradorRepository;

@Service
public class MoradorService {

    private final MoradorRepository moradorRepository;
    private final ComunidadeRepository comunidadeRepository;

    @Autowired
    public MoradorService(MoradorRepository moradorRepository, ComunidadeRepository comunidadeRepository) {
        this.moradorRepository = moradorRepository;
        this.comunidadeRepository = comunidadeRepository;
    }

    @Transactional(readOnly = true)
    public Page<MoradorResponse> findAll(Specification<Morador> spec, Pageable pageable) {
        return moradorRepository.findAll(spec, pageable).map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public MoradorResponse findById(Long id) {
        Morador morador = moradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Morador com ID " + id + " não encontrado."));
        return convertToResponseDto(morador);
    }

    @Transactional
    public MoradorResponse create(MoradorRequest requestDto) {
        Comunidade comunidade = comunidadeRepository.findById(requestDto.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade com ID " + requestDto.comunidadeId() + " não encontrada."));

        // Regra de negócio: contato principal deve ser único
        if (moradorRepository.findByContatoPrincipal(requestDto.contatoPrincipal()).isPresent()) {
            throw new BusinessException("O contato principal '" + requestDto.contatoPrincipal() + "' já está em uso.");
        }

        Morador morador = convertToEntity(requestDto, comunidade);
        Morador savedMorador = moradorRepository.save(morador);
        return convertToResponseDto(savedMorador);
    }

    @Transactional
    public MoradorResponse update(Long id, MoradorRequest requestDto) {
        Morador moradorExistente = moradorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Morador com ID " + id + " não encontrado para atualização."));

        Comunidade comunidade = comunidadeRepository.findById(requestDto.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade com ID " + requestDto.comunidadeId() + " não encontrada."));

        // Regra de negócio: se o contato mudou, verifica se o novo já existe
        if (!moradorExistente.getContatoPrincipal().equals(requestDto.contatoPrincipal())) {
            if (moradorRepository.findByContatoPrincipal(requestDto.contatoPrincipal()).isPresent()) {
                throw new BusinessException(
                        "O contato principal '" + requestDto.contatoPrincipal() + "' já está em uso.");
            }
        }

        // Atualiza os campos
        moradorExistente.setComunidade(comunidade);
        moradorExistente.setNomeMorador(requestDto.nomeMorador());
        moradorExistente.setContatoPrincipal(requestDto.contatoPrincipal());
        moradorExistente.setReceberNotificacoes(requestDto.receberNotificacoes());

        Morador updatedMorador = moradorRepository.save(moradorExistente);
        return convertToResponseDto(updatedMorador);
    }

    @Transactional
    public void delete(Long id) {
        if (!moradorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Morador com ID " + id + " não encontrado para exclusão.");
        }
        moradorRepository.deleteById(id);
    }

    private MoradorResponse convertToResponseDto(Morador morador) {
        return new MoradorResponse(
                morador.getIdMorador(),
                morador.getComunidade().getIdComunidade(),
                morador.getComunidade().getNomeComunidade(),
                morador.getNomeMorador(),
                morador.getContatoPrincipal(),
                morador.getReceberNotificacoes());
    }

    private Morador convertToEntity(MoradorRequest requestDto, Comunidade comunidade) {
        return Morador.builder()
                .comunidade(comunidade)
                .nomeMorador(requestDto.nomeMorador())
                .contatoPrincipal(requestDto.contatoPrincipal())
                .receberNotificacoes(requestDto.receberNotificacoes())
                .build();
    }
}
