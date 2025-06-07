package br.com.fiap.flood_safe_api.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.fiap.flood_safe_api.dto.RestErrorResponse;
import br.com.fiap.flood_safe_api.dto.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

     // Manipula exceções de Recurso Não Encontrado (404 Not Found).
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        log.warn("Recurso não encontrado: {}", ex.getMessage());
        return new RestErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                request.getRequestURI());
    }

    //Manipula exceções de Regras de Negócio (409 Conflict).
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestErrorResponse handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.warn("Conflito de regra de negócio: {}", ex.getMessage());
        return new RestErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Business Rule Conflict",
                ex.getMessage(),
                request.getRequestURI());
    }

    //Manipula exceções de validação de argumentos (@Valid), retornando um detalhamento dos campos inválidos (400 Bad Request).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        log.warn("Erro de validação na requisição: {}", ex.getMessage());
        List<ValidationError> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new RestErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Um ou mais campos são inválidos.",
                request.getRequestURI(),
                validationErrors);
    }

    //Manipulador genérico para qualquer outra exceção não tratada (500 Internal Server Error).
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado na aplicação", ex);
        return new RestErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor. Tente novamente mais tarde.",
                request.getRequestURI());
    }
}
