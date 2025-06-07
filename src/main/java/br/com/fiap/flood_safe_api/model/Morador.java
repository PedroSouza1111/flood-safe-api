package br.com.fiap.flood_safe_api.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Moradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Morador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_morador")
    private Long idMorador;

    @NotNull(message = "Comunidade do morador é obrigatória")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comunidade", nullable = false)
    private Comunidade comunidade;

    @NotBlank(message = "Nome do morador é obrigatório")
    @Size(max = 100, message = "Nome do morador deve ter no máximo 100 caracteres")
    @Column(name = "nome_morador", nullable = false, length = 100)
    private String nomeMorador;

    @NotBlank(message = "Contato principal é obrigatório")
    @Size(max = 50, message = "Contato principal deve ter no máximo 50 caracteres")
    @Column(name = "contato_principal", nullable = false, unique = true, length = 50)
    private String contatoPrincipal;

    @NotNull(message = "Opção de receber notificações é obrigatória")
    @Column(name = "receber_notificacoes", nullable = false, length = 1) // Armazenado como 'S' ou 'N' no DDL
    private char receberNotificacoes;

    @OneToMany(mappedBy = "morador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NotificacaoEnviada> notificacoesRecebidas = new ArrayList<>();
}
