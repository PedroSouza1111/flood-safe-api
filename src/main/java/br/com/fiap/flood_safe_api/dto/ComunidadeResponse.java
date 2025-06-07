package br.com.fiap.flood_safe_api.dto;

import br.com.fiap.flood_safe_api.model.enums.NivelRisco;

public record ComunidadeResponse(
    Long idComunidade,
    String nomeComunidade,
    String regiao,
    Double latitude,
    Double longitude,
    NivelRisco nivelRiscoHistorico
){}
