package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDate;

import br.com.fiap.flood_safe_api.model.enums.StatusSensor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SensorRequest(
    @NotNull(message = "ID da comunidade é obrigatório")
    Long comunidadeId,

    @Size(max = 150)
    String localizacaoEspecifica,

    LocalDate dataInstalacao,

    @NotNull(message = "Status do sensor é obrigatório")
    StatusSensor statusSensor,

    @NotNull(message = "Limite de alerta é obrigatório")
    @Min(value = 1, message = "Limite de alerta deve ser positivo")
    Double limiteAlertaMm
) {}
