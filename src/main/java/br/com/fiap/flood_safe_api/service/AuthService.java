package br.com.fiap.flood_safe_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.flood_safe_api.repository.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O "username" para o Spring Security será o e-mail do nosso usuário.
        return usuarioRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Usuário com e-mail '" + username + "' não encontrado."));
    }
}
