package br.com.fiap.flood_safe_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.fiap.flood_safe_api.dto.UsuarioRequest;
import br.com.fiap.flood_safe_api.dto.UsuarioResponse;
import br.com.fiap.flood_safe_api.exception.BusinessException;
import br.com.fiap.flood_safe_api.model.Usuario;
import br.com.fiap.flood_safe_api.model.enums.UsuarioRole;
import br.com.fiap.flood_safe_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoint para registro de usuários da API.")
@Slf4j
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar um novo usuário")
    public ResponseEntity<UsuarioResponse> create(@RequestBody @Valid UsuarioRequest requestDto,
            UriComponentsBuilder uriBuilder) {
        log.info("Registrando novo usuário com e-mail: {}", requestDto.email());

        // Verifica se o e-mail já está em uso
        if (usuarioRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new BusinessException("O e-mail informado já está em uso.");
        }

        Usuario novoUsuario = Usuario.builder()
                .email(requestDto.email())
                .password(passwordEncoder.encode(requestDto.password()))
                .role(UsuarioRole.USER)
                .build();

        usuarioRepository.save(novoUsuario);

        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(novoUsuario.getId()).toUri();

        var responseDto = new UsuarioResponse(novoUsuario.getId(), novoUsuario.getEmail(), novoUsuario.getRole());

        return ResponseEntity.created(uri).body(responseDto);
    }
}
