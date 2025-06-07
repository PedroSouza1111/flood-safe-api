package br.com.fiap.flood_safe_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}