package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDateTime;

public record LeituraSensorResponse(

        Long idLeitura,
        Long sensorId,
        LocalDateTime dataHoraLeitura,
        Double nivelAguaMm

) {}
