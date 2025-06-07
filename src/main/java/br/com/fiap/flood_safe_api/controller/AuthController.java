package br.com.fiap.flood_safe_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.flood_safe_api.dto.Credentials;
import br.com.fiap.flood_safe_api.dto.Token;
import br.com.fiap.flood_safe_api.model.Usuario;
import br.com.fiap.flood_safe_api.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoint para login e obtenção de token JWT.")
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realizar login e obter token")
    public ResponseEntity<Token> login(@RequestBody @Valid Credentials credentials) {
        log.info("Tentativa de login para o usuário: {}", credentials.email());

        var authToken = new UsernamePasswordAuthenticationToken(credentials.email(), credentials.password());

        Authentication authentication = authenticationManager.authenticate(authToken);

        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();
        Token token = tokenService.createToken(usuarioAutenticado);

        log.info("Login bem-sucedido e token gerado para: {}", credentials.email());
        return ResponseEntity.ok(token);
    }
}
