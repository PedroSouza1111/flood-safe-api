package br.com.fiap.flood_safe_api.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fiap.flood_safe_api.dto.Token;
import br.com.fiap.flood_safe_api.model.Usuario;

@Service
public class TokenService {

    // É uma boa prática injetar o segredo e a validade a partir do
    // application.properties
    @Value("${jwt.secret:um-segredo-muito-forte-para-o-floodsafe}")
    private String jwtSecret;

    @Value("${jwt.expiration.hours:2}")
    private long expirationHours;

    public Token createToken(Usuario usuario) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        Instant expiresAt = Instant.now().plus(expirationHours, ChronoUnit.HOURS);

        String token = JWT.create()
                .withIssuer("FloodSafeAPI")
                .withSubject(usuario.getId().toString())
                .withClaim("email", usuario.getEmail())
                .withClaim("role", usuario.getRole().name())
                .withExpiresAt(expiresAt)
                .sign(algorithm);

        return new Token(token, "Bearer", usuario.getEmail());
    }

    public Usuario getUsuarioFromToken(String jwt) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
        var verifier = JWT.require(algorithm).withIssuer("FloodSafeAPI").build();
        var decodedJwt = verifier.verify(jwt);

        return Usuario.builder()
                .id(Long.valueOf(decodedJwt.getSubject()))
                .email(decodedJwt.getClaim("email").asString())
                .build();
    }
}
