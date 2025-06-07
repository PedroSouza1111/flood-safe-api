package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDateTime;

import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;

public record NotificacaoResponse(
    Long idNotificacao,
    Long alertaId,
    Long moradorId,
    LocalDateTime dataHoraEnvio,
    StatusEntrega statusEntrega
) {}
