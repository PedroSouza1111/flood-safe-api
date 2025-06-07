package br.com.fiap.flood_safe_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MoradorRequest(
    @NotNull(message = "ID da comunidade é obrigatório")
    Long comunidadeId,

    @NotBlank(message = "Nome do morador é obrigatório")
    @Size(max = 100)
    String nomeMorador,

    @NotBlank(message = "Contato principal é obrigatório")
    @Size(max = 50)
    String contatoPrincipal,

    @NotNull(message = "Opção de receber notificações é obrigatória")
    char receberNotificacoes // 'S' ou 'N'
) {}
