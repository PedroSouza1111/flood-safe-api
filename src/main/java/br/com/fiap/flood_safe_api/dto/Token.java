package br.com.fiap.flood_safe_api.dto;

public record Token(
    String token,
    String type,
    String email
) {}
