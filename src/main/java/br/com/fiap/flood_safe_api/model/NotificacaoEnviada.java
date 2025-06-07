package br.com.fiap.flood_safe_api.model;

import java.time.LocalDateTime;

import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Notificacoes_Enviadas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoEnviada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacao")
    private Long idNotificacao;

    @NotNull(message = "Alerta da notificação é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alerta", nullable = false)
    private Alerta alerta;

    @NotNull(message = "Morador da notificação é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_morador", nullable = false)
    private Morador morador;

    @NotNull(message = "Data e hora de envio são obrigatórias")
    @Column(name = "data_hora_envio", nullable = false)
    private LocalDateTime dataHoraEnvio;

    @NotNull(message = "Status de entrega é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_entrega", nullable = false, length = 15)
    private StatusEntrega statusEntrega;
}