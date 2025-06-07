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

import br.com.fiap.flood_safe_api.dto.MoradorRequest;
import br.com.fiap.flood_safe_api.dto.MoradorResponse;
import br.com.fiap.flood_safe_api.model.Morador;
import br.com.fiap.flood_safe_api.service.MoradorService;
import br.com.fiap.flood_safe_api.specification.MoradorSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/moradores")
@Tag(name = "Moradores", description = "Endpoints para gerenciamento de moradores.")
@Slf4j
public class MoradorController {

    private final MoradorService moradorService;

    @Autowired
    public MoradorController(MoradorService moradorService) {
        this.moradorService = moradorService;
    }

    @GetMapping
    @Operation(summary = "Listar todos os moradores")
    public ResponseEntity<Page<MoradorResponse>> getAll(
            @PageableDefault(size = 10, sort = "nomeMorador") Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando moradores com parâmetros: {}", searchParams);
        Specification<Morador> spec = MoradorSpecification.withFilters(searchParams);
        return ResponseEntity.ok(moradorService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar morador por ID")
    public ResponseEntity<MoradorResponse> getById(@PathVariable Long id) {
        log.info("Buscando morador com ID: {}", id);
        return ResponseEntity.ok(moradorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo morador")
    public ResponseEntity<MoradorResponse> create(@RequestBody @Valid MoradorRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando novo morador: {}", requestDto.nomeMorador());
        MoradorResponse response = moradorService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/moradores/{id}").buildAndExpand(response.idMorador()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar morador existente")
    public ResponseEntity<MoradorResponse> update(@PathVariable Long id,
            @RequestBody @Valid MoradorRequest requestDto) {
        log.info("Atualizando morador com ID: {}", id);
        return ResponseEntity.ok(moradorService.update(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar morador por ID")
    public void delete(@PathVariable Long id) {
        log.info("Deletando morador com ID: {}", id);
        moradorService.delete(id);
    }
}
