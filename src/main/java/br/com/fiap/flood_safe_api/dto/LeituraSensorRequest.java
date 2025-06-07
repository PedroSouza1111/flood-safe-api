package br.com.fiap.flood_safe_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LeituraSensorRequest(
    @NotNull(message = "ID do sensor é obrigatório")
    Long sensorId,

    @NotNull(message = "Nível da água é obrigatório")
    @Min(value = 0, message = "Nível da água não pode ser negativo")
    Double nivelAguaMm
) {}
