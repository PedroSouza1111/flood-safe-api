package br.com.fiap.flood_safe_api.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.Morador;
import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.model.Usuario;
import br.com.fiap.flood_safe_api.model.enums.NivelRisco;
import br.com.fiap.flood_safe_api.model.enums.StatusSensor;
import br.com.fiap.flood_safe_api.model.enums.UsuarioRole;
import br.com.fiap.flood_safe_api.repository.ComunidadeRepository;
import br.com.fiap.flood_safe_api.repository.MoradorRepository;
import br.com.fiap.flood_safe_api.repository.SensorRepository;
import br.com.fiap.flood_safe_api.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;

@Configuration
@Profile("dev")
public class DatabaseSeeder {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ComunidadeRepository comunidadeRepository;
    @Autowired
    private SensorRepository sensorRepository;
    @Autowired
    private MoradorRepository moradorRepository;

    @PostConstruct
    public void init() {
        // Criar Usuários da API
        Usuario admin = Usuario.builder().email("admin@floodsafe.com").password(passwordEncoder.encode("123456"))
                .role(UsuarioRole.ADMIN).build();
        Usuario user = Usuario.builder().email("user@floodsafe.com").password(passwordEncoder.encode("123456"))
                .role(UsuarioRole.USER).build();
        usuarioRepository.saveAll(List.of(admin, user));

        // Criar Comunidades
        Comunidade vE = Comunidade.builder().nomeComunidade("Vila Esperança").regiao("Zona Leste")
                .nivelRiscoHistorico(NivelRisco.ALTO).build();
        Comunidade jF = Comunidade.builder().nomeComunidade("Jardim das Flores").regiao("Zona Oeste")
                .nivelRiscoHistorico(NivelRisco.MEDIO).build();
        Comunidade rV = Comunidade.builder().nomeComunidade("Recanto Verde").regiao("Zona Norte")
                .nivelRiscoHistorico(NivelRisco.BAIXO).build();
        comunidadeRepository.saveAll(List.of(vE, jF, rV));

        // Criar Sensores
        Sensor s1 = Sensor.builder().comunidade(vE).localizacaoEspecifica("Ponte Rio Tamanduateí")
                .statusSensor(StatusSensor.ATIVO).dataInstalacao(LocalDate.now()).limiteAlertaMm(1500.0).build();
        Sensor s2 = Sensor.builder().comunidade(jF).localizacaoEspecifica("Margem Córrego Pinheiros")
                .statusSensor(StatusSensor.ATIVO).dataInstalacao(LocalDate.now()).limiteAlertaMm(2000.0).build();
        sensorRepository.saveAll(List.of(s1, s2));

        // Criar Moradores
        Morador m1 = Morador.builder().comunidade(vE).nomeMorador("João Silva").contatoPrincipal("11987654321")
                .receberNotificacoes('S').build();
        Morador m2 = Morador.builder().comunidade(jF).nomeMorador("Maria Oliveira").contatoPrincipal("11912345678")
                .receberNotificacoes('S').build();
        moradorRepository.saveAll(List.of(m1, m2));
    }
}