package br.com.fiap.flood_safe_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record Credentials(
        @NotBlank(message = "O e-mail é obrigatório") 
        @Email(message = "Formato de e-mail inválido") 
        String email,

        @NotBlank(message = "A senha é obrigatória") 
        String password
) {}
