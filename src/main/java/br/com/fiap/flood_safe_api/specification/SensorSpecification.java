package br.com.fiap.flood_safe_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.Sensor;
import br.com.fiap.flood_safe_api.model.enums.StatusSensor;
import jakarta.persistence.criteria.Predicate;

public class SensorSpecification {

    public static Specification<Sensor> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por status do sensor
            String status = params.get("statusSensor");
            if (StringUtils.hasText(status)) {
                try {
                    StatusSensor statusEnum = StatusSensor.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("statusSensor"), statusEnum));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Filtro por ID da comunidade
            String comunidadeId = params.get("comunidadeId");
            if (StringUtils.hasText(comunidadeId)) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("comunidade").get("idComunidade"),
                            Long.parseLong(comunidadeId)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por limite de alerta (maior ou igual a)
            String limiteGte = params.get("limiteAlertaMm_gte");
            if (StringUtils.hasText(limiteGte)) {
                try {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("limiteAlertaMm"),
                            Double.parseDouble(limiteGte)));
                } catch (NumberFormatException ignored) {
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
