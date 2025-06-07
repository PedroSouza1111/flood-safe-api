package br.com.fiap.flood_safe_api.specification;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.NotificacaoEnviada;
import br.com.fiap.flood_safe_api.model.enums.StatusEntrega;
import jakarta.persistence.criteria.Predicate;

public class NotificacaoEnviadaSpecification {

    public static Specification<NotificacaoEnviada> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por ID do alerta
            String alertaId = params.get("alertaId");
            if (StringUtils.hasText(alertaId)) {
                try {
                    predicates.add(criteriaBuilder.equal(root.get("alerta").get("idAlerta"), Long.parseLong(alertaId)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por ID do morador
            String moradorId = params.get("moradorId");
            if (StringUtils.hasText(moradorId)) {
                try {
                    predicates.add(
                            criteriaBuilder.equal(root.get("morador").get("idMorador"), Long.parseLong(moradorId)));
                } catch (NumberFormatException ignored) {
                }
            }

            // Filtro por status de entrega
            String status = params.get("statusEntrega");
            if (StringUtils.hasText(status)) {
                try {
                    StatusEntrega statusEnum = StatusEntrega.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("statusEntrega"), statusEnum));
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Filtro por data de envio (depois de)
            String dataAfter = params.get("dataHoraEnvio_after");
            if (StringUtils.hasText(dataAfter)) {
                try {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataHoraEnvio"),
                            LocalDateTime.parse(dataAfter)));
                } catch (DateTimeParseException ignored) {
                }
            }

            // Filtro por data de envio (antes de)
            String dataBefore = params.get("dataHoraEnvio_before");
            if (StringUtils.hasText(dataBefore)) {
                try {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataHoraEnvio"),
                            LocalDateTime.parse(dataBefore)));
                } catch (DateTimeParseException ignored) {
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
