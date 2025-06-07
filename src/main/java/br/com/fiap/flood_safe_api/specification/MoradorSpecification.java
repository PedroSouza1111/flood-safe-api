package br.com.fiap.flood_safe_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.Morador;
import jakarta.persistence.criteria.Predicate;

public class MoradorSpecification {

    public static Specification<Morador> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome do morador
            String nome = params.get("nomeMorador");
            if (StringUtils.hasText(nome)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomeMorador")),
                        "%" + nome.toLowerCase() + "%"));
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

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
