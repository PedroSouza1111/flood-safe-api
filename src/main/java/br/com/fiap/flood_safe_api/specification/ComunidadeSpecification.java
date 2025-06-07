package br.com.fiap.flood_safe_api.specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.enums.NivelRisco;
import jakarta.persistence.criteria.Predicate;

public class ComunidadeSpecification {

    public static Specification<Comunidade> withFilters(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            if (params == null || params.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nome da comunidade
            String nomeComunidade = params.get("nomeComunidade");
            if (StringUtils.hasText(nomeComunidade)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nomeComunidade")), "%" + nomeComunidade.toLowerCase() + "%"));
            }

            // Filtro por região
            String regiao = params.get("regiao");
            if (StringUtils.hasText(regiao)) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("regiao")), "%" + regiao.toLowerCase() + "%"));
            }

            // Filtro por nível de risco histórico
            String nivelRisco = params.get("nivelRiscoHistorico");
            if (StringUtils.hasText(nivelRisco)) {
                try {
                    NivelRisco nivelRiscoEnum = NivelRisco.valueOf(nivelRisco.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("nivelRiscoHistorico"), nivelRiscoEnum));
                } catch (IllegalArgumentException e) {
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
