package br.com.fiap.flood_safe_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RestErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ValidationError> validationErrors) {
    // Construtor para erros gerais
    public RestErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    // Construtor para erros de validação
    public RestErrorResponse(int status, String error, String message, String path,
            List<ValidationError> validationErrors) {
        this(LocalDateTime.now(), status, error, message, path, validationErrors);
    }
}
