package br.com.fiap.flood_safe_api.dto;

import br.com.fiap.flood_safe_api.model.enums.UsuarioRole;

public record UsuarioResponse(
        Long id,
        String email,
        UsuarioRole role
) {}
