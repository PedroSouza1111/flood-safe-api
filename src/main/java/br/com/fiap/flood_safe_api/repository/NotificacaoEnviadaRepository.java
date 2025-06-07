package br.com.fiap.flood_safe_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.model.Morador;
import br.com.fiap.flood_safe_api.model.NotificacaoEnviada;
import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;

@Repository
public interface NotificacaoEnviadaRepository extends JpaRepository<NotificacaoEnviada, Long>, JpaSpecificationExecutor<NotificacaoEnviada> {

    /**
     * Encontra todas as notificações enviadas para um alerta específico, com
     * paginação.
     * 
     * @param alertaId O ID do alerta.
     * @param pageable Configurações de paginação e ordenação.
     * @return Uma página de notificações enviadas.
     */
    Page<NotificacaoEnviada> findByAlertaIdAlerta(Long alertaId, Pageable pageable);

    /**
     * Encontra todas as notificações enviadas para um morador específico, com
     * paginação.
     * 
     * @param moradorId O ID do morador.
     * @param pageable  Configurações de paginação e ordenação.
     * @return Uma página de notificações enviadas.
     */
    Page<NotificacaoEnviada> findByMoradorIdMorador(Long moradorId, Pageable pageable);

    /**
     * Encontra todas as notificações com um status de entrega específico.
     * 
     * @param statusEntrega O status da entrega.
     * @return Uma lista de notificações enviadas.
     */
    List<NotificacaoEnviada> findByStatusEntrega(StatusEntrega statusEntrega);

    /**
     * Encontra todas as notificações associadas a um alerta.
     * 
     * @param alerta A entidade Alerta.
     * @return Uma lista de notificações.
     */
    List<NotificacaoEnviada> findByAlerta(Alerta alerta);

    /**
     * Encontra todas as notificações associadas a um morador.
     * 
     * @param morador A entidade Morador.
     * @return Uma lista de notificações.
     */
    List<NotificacaoEnviada> findByMorador(Morador morador);
}
