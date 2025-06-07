package br.com.fiap.flood_safe_api.dto;

public record MoradorResponse(
    Long idMorador,
    Long comunidadeId,
    String nomeComunidade,
    String nomeMorador,
    String contatoPrincipal,
    char receberNotificacoes
) {}
