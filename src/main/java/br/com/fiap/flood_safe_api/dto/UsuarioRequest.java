package br.com.fiap.flood_safe_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "O e-mail é obrigatório") 
        @Email(message = "Formato de e-mail inválido") 
        String email,

        @NotBlank(message = "A senha é obrigatória") 
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres") 
        String password
) {}
