package br.com.fiap.flood_safe_api.dto;

import br.com.fiap.flood_safe_api.model.enums.NivelRisco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ComunidadeRequest(
    @NotBlank(message = "Nome da comunidade é obrigatório")
    @Size(max = 100)
    String nomeComunidade,

    @NotBlank(message = "Região é obrigatória")
    @Size(max = 50)
    String regiao,

    Double latitude,
    Double longitude,

    @NotNull(message = "Nível de risco histórico é obrigatório")
    NivelRisco nivelRiscoHistorico
){}
