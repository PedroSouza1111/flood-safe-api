package br.com.fiap.flood_safe_api.model;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.flood_safe_api.model.enums.NivelRisco;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Comunidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comunidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comunidade")
    private Long idComunidade;

    @NotBlank(message = "Nome da comunidade é obrigatório")
    @Size(max = 100, message = "Nome da comunidade deve ter no máximo 100 caracteres")
    @Column(name = "nome_comunidade", nullable = false, length = 100)
    private String nomeComunidade;

    @NotBlank(message = "Região é obrigatória")
    @Size(max = 50, message = "Região deve ter no máximo 50 caracteres")
    @Column(name = "regiao", nullable = false, length = 50)
    private String regiao;

    @Column(name = "latitude")
    private Double latitude; // Ou BigDecimal para maior precisão

    @Column(name = "longitude")
    private Double longitude; // Ou BigDecimal para maior precisão

    @NotNull(message = "Nível de risco histórico é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_risco_historico", nullable = false, length = 10)
    private NivelRisco nivelRiscoHistorico;

    @OneToMany(mappedBy = "comunidade", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default // Para inicializar com o builder do Lombok
    private List<Sensor> sensores = new ArrayList<>();

    @OneToMany(mappedBy = "comunidade", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Morador> moradores = new ArrayList<>();

    // Se um alerta está diretamente ligado à comunidade afetada (o que faz sentido)
    @OneToMany(mappedBy = "comunidadeAfetada", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Alerta> alertas = new ArrayList<>();
}
