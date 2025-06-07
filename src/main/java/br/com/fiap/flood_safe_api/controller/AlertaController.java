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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.AlertaRequest;
import br.com.fiap.flood_safe_api.dto.AlertaResponse;
import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.service.AlertaService;
import br.com.fiap.flood_safe_api.specification.AlertaSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/alertas")
@Tag(name = "Alertas", description = "Endpoints para visualização e criação de alertas de enchente.")
@Slf4j
public class AlertaController {

    private final AlertaService alertaService;

    @Autowired
    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os alertas")
    public ResponseEntity<Page<AlertaResponse>> getAll(
            @PageableDefault(size = 10, sort = "dataHoraAlerta", direction = org.springframework.data.domain.Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando alertas com parâmetros: {}", searchParams);
        Specification<Alerta> spec = AlertaSpecification.withFilters(searchParams);
        return ResponseEntity.ok(alertaService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<AlertaResponse> getById(@PathVariable Long id) {
        log.info("Buscando alerta com ID: {}", id);
        return ResponseEntity.ok(alertaService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo alerta")
    public ResponseEntity<AlertaResponse> create(@RequestBody @Valid AlertaRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando novo alerta para comunidade ID {}", requestDto.comunidadeAfetadaId());
        AlertaResponse response = alertaService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/alertas/{id}").buildAndExpand(response.idAlerta()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar alerta por ID")
    public void delete(@PathVariable Long id) {
        log.info("Deletando alerta com ID: {}", id);
        alertaService.delete(id);
    }
}
