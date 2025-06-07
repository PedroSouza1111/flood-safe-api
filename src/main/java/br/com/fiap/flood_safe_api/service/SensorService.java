package br.com.fiap.flood_safe_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.SensorRequest;
import br.com.fiap.flood_safe_api.dto.SensorResponse;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.repository.ComunidadeRepository;
import br.com.fiap.flood_safe_api.repository.SensorRepository;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final ComunidadeRepository comunidadeRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository, ComunidadeRepository comunidadeRepository) {
        this.sensorRepository = sensorRepository;
        this.comunidadeRepository = comunidadeRepository;
    }

    @Transactional(readOnly = true)
    public Page<SensorResponse> findAll(Specification<Sensor> spec, Pageable pageable) {
        return sensorRepository.findAll(spec, pageable).map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public SensorResponse findById(Long id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor com ID " + id + " não encontrado."));
        return convertToResponseDto(sensor);
    }

    @Transactional
    public SensorResponse create(SensorRequest requestDto) {
        Comunidade comunidade = comunidadeRepository.findById(requestDto.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade com ID " + requestDto.comunidadeId() + " não encontrada."));

        Sensor sensor = convertToEntity(requestDto, comunidade);
        Sensor savedSensor = sensorRepository.save(sensor);
        return convertToResponseDto(savedSensor);
    }

    @Transactional
    public SensorResponse update(Long id, SensorRequest requestDto) {
        Sensor sensorExistente = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sensor com ID " + id + " não encontrado para atualização."));

        Comunidade comunidade = comunidadeRepository.findById(requestDto.comunidadeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comunidade com ID " + requestDto.comunidadeId() + " não encontrada."));

        sensorExistente.setComunidade(comunidade);
        sensorExistente.setLocalizacaoEspecifica(requestDto.localizacaoEspecifica());
        sensorExistente.setDataInstalacao(requestDto.dataInstalacao());
        sensorExistente.setStatusSensor(requestDto.statusSensor());
        sensorExistente.setLimiteAlertaMm(requestDto.limiteAlertaMm());

        Sensor updatedSensor = sensorRepository.save(sensorExistente);
        return convertToResponseDto(updatedSensor);
    }

    @Transactional
    public void delete(Long id) {
        if (!sensorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sensor com ID " + id + " não encontrado para exclusão.");
        }
        sensorRepository.deleteById(id);
    }

    private SensorResponse convertToResponseDto(Sensor sensor) {
        return new SensorResponse(
                sensor.getIdSensor(),
                sensor.getComunidade().getIdComunidade(),
                sensor.getComunidade().getNomeComunidade(),
                sensor.getLocalizacaoEspecifica(),
                sensor.getDataInstalacao(),
                sensor.getStatusSensor(),
                sensor.getLimiteAlertaMm());
    }

    private Sensor convertToEntity(SensorRequest requestDto, Comunidade comunidade) {
        return Sensor.builder()
                .comunidade(comunidade)
                .localizacaoEspecifica(requestDto.localizacaoEspecifica())
                .dataInstalacao(requestDto.dataInstalacao())
                .statusSensor(requestDto.statusSensor())
                .limiteAlertaMm(requestDto.limiteAlertaMm())
                .build();
    }
}
