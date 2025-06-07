package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDateTime;

import br.com.fiap.flood_safe_api.model.enums.TipoAlerta;

public record AlertaResponse(

        Long idAlerta,
        Long leituraGatilhoId,
        Long comunidadeAfetadaId,
        String nomeComunidade, // Adicionando contexto
        LocalDateTime dataHoraAlerta,
        TipoAlerta tipoAlerta,
        String mensagemAlerta
) {}
