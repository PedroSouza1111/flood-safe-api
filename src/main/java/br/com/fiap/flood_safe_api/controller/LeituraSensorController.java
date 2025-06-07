package br.com.fiap.flood_safe_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.LeituraSensorRequest;
import br.com.fiap.flood_safe_api.dto.LeituraSensorResponse;
import br.com.fiap.flood_safe_api.model.LeituraSensor;
import br.com.fiap.flood_safe_api.service.LeituraSensorService;
import br.com.fiap.flood_safe_api.specification.LeituraSensorSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/leituras")
@Tag(name = "Leituras de Sensor", description = "Endpoints para registro e consulta de leituras de sensores.")
@Slf4j
public class LeituraSensorController {

    private final LeituraSensorService leituraSensorService;

    @Autowired
    public LeituraSensorController(LeituraSensorService leituraSensorService) {
        this.leituraSensorService = leituraSensorService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as leituras de sensores")
    public ResponseEntity<Page<LeituraSensorResponse>> getAll(
            @PageableDefault(size = 20, sort = "dataHoraLeitura", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando leituras com parâmetros: {}", searchParams);
        Specification<LeituraSensor> spec = LeituraSensorSpecification.withFilters(searchParams);
        return ResponseEntity.ok(leituraSensorService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar leitura por ID")
    public ResponseEntity<LeituraSensorResponse> getById(@PathVariable Long id) {
        log.info("Buscando leitura com ID: {}", id);
        return ResponseEntity.ok(leituraSensorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registrar nova leitura de sensor")
    public ResponseEntity<LeituraSensorResponse> create(@RequestBody @Valid LeituraSensorRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Registrando nova leitura para o sensor ID {}", requestDto.sensorId());
        LeituraSensorResponse response = leituraSensorService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/leituras/{id}").buildAndExpand(response.idLeitura()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar leitura por ID")
    public void delete(@PathVariable Long id) {
        log.info("Deletando leitura com ID: {}", id);
        leituraSensorService.delete(id);
    }
}