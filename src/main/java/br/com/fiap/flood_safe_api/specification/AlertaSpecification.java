package br.com.fiap.flood_safe_api.specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.Alerta;
import br.com.fiap.flood_safe_api.model.enums.TipoAlerta;
import jakarta.persistence.criteria.Predicate;

public class AlertaSpecification {

    public static Specification<Alerta> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por tipo de alerta
            String tipo = params.get("tipoAlerta");
            if (StringUtils.hasText(tipo)) {
                try {
                    TipoAlerta tipoEnum = TipoAlerta.valueOf(tipo.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("tipoAlerta"), tipoEnum));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Filtro por ID da comunidade afetada
            String comunidadeId = params.get("comunidadeAfetadaId");
            if (StringUtils.hasText(comunidadeId)) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("comunidadeAfetada").get("idComunidade"),
                            Long.parseLong(comunidadeId)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por data (depois de)
            String dataAfter = params.get("dataHoraAlerta_after");
            if (StringUtils.hasText(dataAfter)) {
                try {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraAlerta"),
                            LocalDateTime.parse(dataAfter)));
                } catch (DateTimeParseException ignored) {
                }
            }

            // Filtro por data (antes de)
            String dataBefore = params.get("dataHoraAlerta_before");
            if (StringUtils.hasText(dataBefore)) {
                try {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraAlerta"),
                            LocalDateTime.parse(dataBefore)));
                } catch (DateTimeParseException ignored) {
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
