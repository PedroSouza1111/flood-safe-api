package br.com.fiap.flood_safe_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.SensorRequest;
import br.com.fiap.flood_safe_api.dto.SensorResponse;
import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.service.SensorService;
import br.com.fiap.flood_safe_api.specification.SensorSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/sensores")
@Tag(name = "Sensores", description = "Endpoints para gerenciamento de sensores de nível de água.")
@Slf4j
public class SensorController {

    private final SensorService sensorService;

    @Autowired
    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os sensores", description = "Retorna uma lista paginada de sensores com filtros.")
    public ResponseEntity<Page<SensorResponse>> getAll(
            @PageableDefault(size = 10, sort = "localizacaoEspecifica") Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando sensores com parâmetros: {}", searchParams);
        Specification<Sensor> spec = SensorSpecification.withFilters(searchParams);
        Page<SensorResponse> responsePage = sensorService.findAll(spec, pageable);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por ID")
    public ResponseEntity<SensorResponse> getById(@PathVariable Long id) {
        log.info("Buscando sensor com ID: {}", id);
        return ResponseEntity.ok(sensorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo sensor")
    public ResponseEntity<SensorResponse> create(@RequestBody @Valid SensorRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando novo sensor: {}", requestDto.localizacaoEspecifica());
        SensorResponse response = sensorService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/sensores/{id}").buildAndExpand(response.idSensor()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar sensor existente")
    public ResponseEntity<SensorResponse> update(@PathVariable Long id, @RequestBody @Valid SensorRequest requestDto) {
        log.info("Atualizando sensor com ID: {}", id);
        return ResponseEntity.ok(sensorService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar sensor por ID")
    public void delete(@PathVariable Long id) {
        log.info("Deletando sensor com ID: {}", id);
        sensorService.delete(id);
    }
}
