package br.com.fiap.flood_safe_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.flood_safe_api.model.LeituraSensor;
import br.com.fiap.flood_safe_api.model.Sensor;

@Repository
public interface LeituraSensorRepository
        extends JpaRepository<LeituraSensor, Long>, JpaSpecificationExecutor<LeituraSensor> {

    /**
     * Encontra todas as leituras de um sensor específico, com paginação.
     * 
     * @param sensorId O ID do sensor.
     * @param pageable Configurações de paginação e ordenação.
     * @return Uma página de leituras de sensor.
     */
    Page<LeituraSensor> findBySensorIdSensor(Long sensorId, Pageable pageable);

    /**
     * Encontra todas as leituras de um sensor específico.
     * 
     * @param sensor A entidade Sensor.
     * @return Uma lista de leituras de sensor.
     */
    List<LeituraSensor> findBySensor(Sensor sensor);

    /**
     * Encontra leituras de sensor dentro de um intervalo de datas.
     * 
     * @param inicio Data e hora de início do intervalo.
     * @param fim    Data e hora de fim do intervalo.
     * @return Uma lista de leituras de sensor.
     */
    List<LeituraSensor> findByDataHoraLeituraBetween(LocalDateTime inicio, LocalDateTime fim);
}
