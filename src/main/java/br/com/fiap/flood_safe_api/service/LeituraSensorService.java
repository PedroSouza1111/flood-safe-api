package br.com.fiap.flood_safe_api.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.flood_safe_api.dto.LeituraSensorRequest;
import br.com.fiap.flood_safe_api.dto.LeituraSensorResponse;
import br.com.fiap.flood_safe_api.exception.ResourceNotFoundException;
import br.com.fiap.flood_safe_api.model.LeituraSensor;
import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.repository.LeituraSensorRepository;
import br.com.fiap.flood_safe_api.repository.SensorRepository;

@Service
public class LeituraSensorService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public LeituraSensorService(LeituraSensorRepository leituraSensorRepository, SensorRepository sensorRepository) {
        this.leituraSensorRepository = leituraSensorRepository;
        this.sensorRepository = sensorRepository;
    }

    @Transactional(readOnly = true)
    public Page<LeituraSensorResponse> findAll(Specification<LeituraSensor> spec, Pageable pageable) {
        return leituraSensorRepository.findAll(spec, pageable).map(this::convertToResponseDto);
    }

    @Transactional(readOnly = true)
    public LeituraSensorResponse findById(Long id) {
        LeituraSensor leitura = leituraSensorRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Leitura de sensor com ID " + id + " não encontrada."));
        return convertToResponseDto(leitura);
    }

    @Transactional
    public LeituraSensorResponse create(LeituraSensorRequest requestDto) {
        Sensor sensor = sensorRepository.findById(requestDto.sensorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sensor com ID " + requestDto.sensorId() + " não encontrado."));

        LeituraSensor leitura = convertToEntity(requestDto, sensor);

        leitura.setDataHoraLeitura(LocalDateTime.now());

        LeituraSensor savedLeitura = leituraSensorRepository.save(leitura);
        return convertToResponseDto(savedLeitura);
    }

    @Transactional
    public void delete(Long id) {
        if (!leituraSensorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Leitura de sensor com ID " + id + " não encontrada para exclusão.");
        }
        leituraSensorRepository.deleteById(id);
    }

    private LeituraSensorResponse convertToResponseDto(LeituraSensor leitura) {
        return new LeituraSensorResponse(
                leitura.getIdLeitura(),
                leitura.getSensor().getIdSensor(),
                leitura.getDataHoraLeitura(),
                leitura.getNivelAguaMm());
    }

    private LeituraSensor convertToEntity(LeituraSensorRequest requestDto, Sensor sensor) {
        return LeituraSensor.builder()
                .sensor(sensor)
                .nivelAguaMm(requestDto.nivelAguaMm())
                .build();
    }
}
