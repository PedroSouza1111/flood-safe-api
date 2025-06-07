package br.com.fiap.flood_safe_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.LeituraSensor;
import br.com.fiap.flood_safe_api.model.enums.TipoAlerta;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long>, JpaSpecificationExecutor<Alerta> {

    /**
     * Encontra todos os alertas para uma comunidade afetada específica, com
     * paginação.
     * 
     * @param comunidadeId O ID da comunidade afetada.
     * @param pageable     Configurações de paginação e ordenação.
     * @return Uma página de alertas.
     */
    Page<Alerta> findByComunidadeAfetadaIdComunidade(Long comunidadeId, Pageable pageable);

    /**
     * Encontra todos os alertas de um tipo específico.
     * 
     * @param tipoAlerta O tipo do alerta.
     * @return Uma lista de alertas.
     */
    List<Alerta> findByTipoAlerta(TipoAlerta tipoAlerta);

    /**
     * Encontra um alerta pela sua leitura gatilho.
     * 
     * @param leituraGatilho A entidade LeituraSensor que disparou o alerta.
     * @return Um Optional contendo o alerta se encontrado.
     */
    Optional<Alerta> findByLeituraGatilho(LeituraSensor leituraGatilho);

    /**
     * Encontra todos os alertas associados a uma comunidade afetada.
     * 
     * @param comunidadeAfetada A entidade Comunidade.
     * @return Uma lista de alertas.
     */
    List<Alerta> findByComunidadeAfetada(Comunidade comunidadeAfetada);
}
