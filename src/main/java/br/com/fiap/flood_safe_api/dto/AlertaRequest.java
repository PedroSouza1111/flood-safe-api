package br.com.fiap.flood_safe_api.dto;

import br.com.fiap.flood_safe_api.model.enums.TipoAlerta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlertaRequest(
        @NotNull(message = "ID da leitura gatilho é obrigatório") Long leituraGatilhoId,

        @NotNull(message = "ID da comunidade afetada é obrigatório") Long comunidadeAfetadaId,

        @NotNull(message = "Tipo do alerta é obrigatório") TipoAlerta tipoAlerta,

        @Size(max = 500) String mensagemAlerta
) {}
