package br.com.fiap.flood_safe_api.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.model.enums.StatusSensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long>, JpaSpecificationExecutor<Sensor> {

    /**
     * Encontra todos os sensores associados a uma comunidade específica, com
     * paginação.
     * 
     * @param comunidadeId O ID da comunidade.
     * @param pageable     Configurações de paginação e ordenação.
     * @return Uma página de sensores.
     */
    Page<Sensor> findByComunidadeIdComunidade(Long comunidadeId, Pageable pageable);

    /**
     * Encontra todos os sensores com um status específico.
     * 
     * @param statusSensor O status do sensor.
     * @return Uma lista de sensores.
     */
    List<Sensor> findByStatusSensor(StatusSensor statusSensor);

    /**
     * Encontra todos os sensores associados a uma comunidade.
     * 
     * @param comunidade A entidade Comunidade.
     * @return Uma lista de sensores.
     */
    List<Sensor> findByComunidade(Comunidade comunidade);
}
