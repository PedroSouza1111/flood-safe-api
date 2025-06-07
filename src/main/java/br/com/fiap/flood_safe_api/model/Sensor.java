package br.com.fiap.flood_safe_api.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.fiap.flood_safe_api.model.enums.StatusSensor;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Sensores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor")
    private Long idSensor;

    @NotNull(message = "Comunidade do sensor é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comunidade", nullable = false)
    private Comunidade comunidade;

    @Size(max = 150, message = "Localização específica deve ter no máximo 150 caracteres")
    @Column(name = "localizacao_especifica", length = 150)
    private String localizacaoEspecifica;

    @Column(name = "data_instalacao")
    private LocalDate dataInstalacao;

    @NotNull(message = "Status do sensor é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "status_sensor", nullable = false, length = 15)
    private StatusSensor statusSensor;

    @NotNull(message = "Limite de alerta em mm é obrigatório")
    @Min(value = 1, message = "Limite de alerta deve ser positivo")
    @Column(name = "limite_alerta_mm", nullable = false)
    private Double limiteAlertaMm;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LeituraSensor> leituras = new ArrayList<>();
}
