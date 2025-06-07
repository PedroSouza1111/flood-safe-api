package br.com.fiap.flood_safe_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Leituras_Sensor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraSensor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leitura")
    private Long idLeitura;

    @NotNull(message = "Sensor da leitura é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    @NotNull(message = "Data e hora da leitura são obrigatórias")
    @Column(name = "data_hora_leitura", nullable = false)
    private LocalDateTime dataHoraLeitura;

    @NotNull(message = "Nível da água é obrigatório")
    @Min(value = 0, message = "Nível da água não pode ser negativo")
    @Column(name = "nivel_agua_mm", nullable = false)
    private Double nivelAguaMm;

    // Opcional: Se quiser uma relação bidirecional com Alerta (Alerta tem o
    // JoinColumn)
    // @OneToOne(mappedBy = "leituraGatilho", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // private Alerta alertaGerado;
}
