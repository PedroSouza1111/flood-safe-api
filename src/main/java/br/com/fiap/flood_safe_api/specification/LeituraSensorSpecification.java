package br.com.fiap.flood_safe_api.specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.LeituraSensor;
import jakarta.persistence.criteria.Predicate;

public class LeituraSensorSpecification {

    public static Specification<LeituraSensor> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por ID do sensor
            String sensorId = params.get("sensorId");
            if (StringUtils.hasText(sensorId)) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("sensor").get("idSensor"), Long.parseLong(sensorId)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por data (depois de)
            String dataAfter = params.get("dataHoraLeitura_after");
            if (StringUtils.hasText(dataAfter)) {
                try {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraLeitura"),
                            LocalDateTime.parse(dataAfter)));
                } catch (DateTimeParseException ignored) {
                }
            }

            // Filtro por data (antes de)
            String dataBefore = params.get("dataHoraLeitura_before");
            if (StringUtils.hasText(dataBefore)) {
                try {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraLeitura"),
                            LocalDateTime.parse(dataBefore)));
                } catch (DateTimeParseException ignored) {
                }
            }

            // Filtro por nível da água (maior ou igual a)
            String nivelGte = params.get("nivelAguaMm_gte");
            if (StringUtils.hasText(nivelGte)) {
                try {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("nivelAguaMm"),
                            Double.parseDouble(nivelGte)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por nível da água (menor ou igual a)
            String nivelLte = params.get("nivelAguaMm_lte");
            if (StringUtils.hasText(nivelLte)) {
                try {
                    predicates.add(
                            criteriaBuilder.lessThanOrEqualTo(root.get("nivelAguaMm"), Double.parseDouble(nivelLte)));
                } catch (NumberFormatException ignored) {
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
