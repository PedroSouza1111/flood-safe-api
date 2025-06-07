package br.com.fiap.flood_safe_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.ComunidadeRequest;
import br.com.fiap.flood_safe_api.dto.ComunidadeResponse;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.service.ComunidadeService;
import br.com.fiap.flood_safe_api.specification.ComunidadeSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/comunidades")
@Tag(name = "Comunidades", description = "Endpoints para gerenciamento de comunidades em áreas de risco.")
@Slf4j
public class ComunidadeController {

    private final ComunidadeService comunidadeService;

    @Autowired
    public ComunidadeController(ComunidadeService comunidadeService) {
        this.comunidadeService = comunidadeService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as comunidades", description = "Retorna uma lista paginada de comunidades, com suporte a filtros dinâmicos e ordenação.")
    public ResponseEntity<Page<ComunidadeResponse>> getAll(
            @PageableDefault(size = 10, sort = "nomeComunidade") Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando comunidades com parâmetros: {} e paginação: {}", searchParams, pageable);
        Specification<Comunidade> spec = ComunidadeSpecification.withFilters(searchParams);
        Page<ComunidadeResponse> responsePage = comunidadeService.findAll(spec, pageable);
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar comunidade por ID", description = "Retorna os detalhes de uma comunidade específica pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comunidade encontrada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Comunidade não encontrada.")
    })
    public ResponseEntity<ComunidadeResponse> getById(@PathVariable Long id) {
        log.info("Buscando comunidade com ID: {}", id);
        ComunidadeResponse response = comunidadeService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Criar nova comunidade", description = "Registra uma nova comunidade no sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Comunidade criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos."),
            @ApiResponse(responseCode = "409", description = "Comunidade com este nome já existe.")
    })
    public ResponseEntity<ComunidadeResponse> create(
            @RequestBody @Valid ComunidadeRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando nova comunidade: {}", requestDto.nomeComunidade());
        ComunidadeResponse response = comunidadeService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/comunidades/{id}").buildAndExpand(response.idComunidade()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar comunidade", description = "Atualiza os dados de uma comunidade existente.")
    public ResponseEntity<ComunidadeResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ComunidadeRequest requestDto) {
        log.info("Atualizando comunidade com ID: {}", id);
        ComunidadeResponse response = comunidadeService.update(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar comunidade", description = "Remove uma comunidade do sistema pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Comunidade deletada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Comunidade não encontrada.")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deletando comunidade com ID: {}", id);
        comunidadeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
