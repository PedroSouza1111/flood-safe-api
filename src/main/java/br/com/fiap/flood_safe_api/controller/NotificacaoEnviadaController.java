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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.NotificacaoRequest;
import br.com.fiap.flood_safe_api.dto.NotificacaoResponse;
import br.com.fiap.flood_safe_api.dto.NotificacaoStatusUpdateRequest;
import br.com.fiap.flood_safe_api.model.NotificacaoEnviada;
import br.com.fiap.flood_safe_api.service.NotificacaoEnviadaService;
import br.com.fiap.flood_safe_api.specification.NotificacaoEnviadaSpecification;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações enviadas.")
@Slf4j
public class NotificacaoEnviadaController {

    private final NotificacaoEnviadaService notificacaoService;

    @Autowired
    public NotificacaoEnviadaController(NotificacaoEnviadaService notificacaoService) {
        this.notificacaoService = notificacaoService;
    }

    @GetMapping
    @Operation(summary = "Listar todas as notificações")
    public ResponseEntity<Page<NotificacaoResponse>> getAll(
            @PageableDefault(size = 20, sort = "dataHoraEnvio", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) Map<String, String> searchParams) {
        log.info("Buscando notificações com parâmetros: {}", searchParams);
        Specification<NotificacaoEnviada> spec = NotificacaoEnviadaSpecification.withFilters(searchParams);
        return ResponseEntity.ok(notificacaoService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificação por ID")
    public ResponseEntity<NotificacaoResponse> getById(@PathVariable Long id) {
        log.info("Buscando notificação com ID: {}", id);
        return ResponseEntity.ok(notificacaoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar nova notificação")
    public ResponseEntity<NotificacaoResponse> create(@RequestBody @Valid NotificacaoRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Criando notificação para o alerta ID {}", requestDto.alertaId());
        NotificacaoResponse response = notificacaoService.create(requestDto);
        var uri = uriBuilder.path("/api/v1/notificacoes/{id}").buildAndExpand(response.idNotificacao()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar o status de uma notificação")
    public ResponseEntity<NotificacaoResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid NotificacaoStatusUpdateRequest requestDto) {
        log.info("Atualizando status da notificação ID {} para {}", id, requestDto.statusEntrega());
        NotificacaoResponse response = notificacaoService.updateStatus(id, requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar notificação por ID")
    public void delete(@PathVariable Long id) {
        log.info("Deletando notificação com ID: {}", id);
        notificacaoService.delete(id);
    }
}
