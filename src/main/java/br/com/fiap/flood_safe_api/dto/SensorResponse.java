package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDate;

import br.com.fiap.flood_safe_api.model.enums.StatusSensor;

public record SensorResponse(

        Long idSensor,
        Long comunidadeId,
        String nomeComunidade,
        String localizacaoEspecifica,
        LocalDate dataInstalacao,
        StatusSensor statusSensor,
        Double limiteAlertaMm

) {}
