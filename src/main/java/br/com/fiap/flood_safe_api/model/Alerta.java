package br.com.fiap.flood_safe_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.flood_safe_api.model.enums.TipoAlerta;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @NotNull(message = "Leitura gatilho do alerta é obrigatória")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_leitura_gatilho", nullable = false, unique = true)
    private LeituraSensor leituraGatilho;

    @NotNull(message = "Comunidade afetada pelo alerta é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comunidade_afetada", nullable = false)
    private Comunidade comunidadeAfetada;

    @NotNull(message = "Data e hora do alerta são obrigatórias")
    @Column(name = "data_hora_alerta", nullable = false)
    private LocalDateTime dataHoraAlerta;

    @NotNull(message = "Tipo do alerta é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alerta", nullable = false, length = 10)
    private TipoAlerta tipoAlerta;

    @Size(max = 500, message = "Mensagem do alerta deve ter no máximo 500 caracteres")
    @Column(name = "mensagem_alerta", length = 500)
    private String mensagemAlerta;

    @OneToMany(mappedBy = "alerta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificacaoEnviada> notificacoesEnviadas = new ArrayList<>();

}
