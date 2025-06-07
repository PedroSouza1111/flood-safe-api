package br.com.fiap.flood_safe_api.dto;

import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;
import jakarta.validation.constraints.NotNull;

public record NotificacaoStatusUpdateRequest(
    @NotNull(message = "Status de entrega é obrigatório")
    StatusEntrega statusEntrega
) {}
