package br.com.fiap.flood_safe_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.fiap.flood_safe_api.model.Comunidade;

public interface ComunidadeRepository extends JpaRepository<Comunidade, Long>, JpaSpecificationExecutor<Comunidade> {
    /**
     * Encontra uma comunidade pelo seu nome.
     * 
     * @param nomeComunidade O nome da comunidade.
     * @return Um Optional contendo a comunidade se encontrada, ou vazio caso
     *         contrário.
     */
    Optional<Comunidade> findByNomeComunidade(String nomeComunidade);

    // Você pode adicionar outros métodos de consulta específicos aqui, se
    // necessário.
    // Ex: Page<Comunidade> findByRegiao(String regiao, Pageable pageable);
}
