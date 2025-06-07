package br.com.fiap.flood_safe_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.ComunidadeRequest;
import br.com.fiap.flood_safe_api.dto.ComunidadeResponse;
import br.com.fiap.flood_safe_api.exception.BusinessException;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.repository.ComunidadeRepository;

@Service
public class ComunidadeService {

    private final ComunidadeRepository comunidadeRepository;

    @Autowired
    public ComunidadeService(ComunidadeRepository comunidadeRepository) {
        this.comunidadeRepository = comunidadeRepository;
    }

    @Transactional(readOnly = true)
    public Page<ComunidadeResponse> findAll(Specification<Comunidade> spec, Pageable pageable) {
        Page<Comunidade> comunidadesPage = comunidadeRepository.findAll(spec, pageable);
        return comunidadesPage.map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public ComunidadeResponse findById(Long id) {
        Comunidade comunidade = comunidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comunidade com ID " + id + " não encontrada."));
        return convertToResponseDto(comunidade);
    }

    @Transactional
    public ComunidadeResponse create(ComunidadeRequest requestDto) {
        
        if (comunidadeRepository.findByNomeComunidade(requestDto.nomeComunidade()).isPresent()) {
            throw new BusinessException("Já existe uma comunidade com o nome: " + requestDto.nomeComunidade());
        }

        Comunidade comunidade = convertToEntity(requestDto);
        Comunidade savedComunidade = comunidadeRepository.save(comunidade);
        return convertToResponseDto(savedComunidade);
    }

    @Transactional
    public ComunidadeResponse update(Long id, ComunidadeRequest requestDto) {
        Comunidade comunidadeExistente = comunidadeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade com ID " + id + " não encontrada para atualização."));

        comunidadeExistente.setNomeComunidade(requestDto.nomeComunidade());
        comunidadeExistente.setRegiao(requestDto.regiao());
        comunidadeExistente.setLatitude(requestDto.latitude());
        comunidadeExistente.setLongitude(requestDto.longitude());
        comunidadeExistente.setNivelRiscoHistorico(requestDto.nivelRiscoHistorico());

        Comunidade updatedComunidade = comunidadeRepository.save(comunidadeExistente);
        return convertToResponseDto(updatedComunidade);
    }

    @Transactional
    public void delete(Long id) {
        if (!comunidadeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comunidade com ID " + id + " não encontrada para exclusão.");
        }
        comunidadeRepository.deleteById(id);
    }

    private ComunidadeResponse convertToResponseDto(Comunidade comunidade) {
        return new ComunidadeResponse(
                comunidade.getIdComunidade(),
                comunidade.getNomeComunidade(),
                comunidade.getRegiao(),
                comunidade.getLatitude(),
                comunidade.getLongitude(),
                comunidade.getNivelRiscoHistorico());
    }

    private Comunidade convertToEntity(ComunidadeRequest requestDto) {
        return Comunidade.builder()
                .nomeComunidade(requestDto.nomeComunidade())
                .regiao(requestDto.regiao())
                .latitude(requestDto.latitude())
                .longitude(requestDto.longitude())
                .nivelRiscoHistorico(requestDto.nivelRiscoHistorico())
                .build();
    }
}
