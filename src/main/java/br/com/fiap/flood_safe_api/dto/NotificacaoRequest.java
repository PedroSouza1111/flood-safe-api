package br.com.fiap.flood_safe_api.dto;

import jakarta.validation.constraints.NotNull;

public record NotificacaoRequest(
    @NotNull(message = "ID do alerta é obrigatório")
    Long alertaId,

    @NotNull(message = "ID do morador é obrigatório")
    Long moradorId
) {}
