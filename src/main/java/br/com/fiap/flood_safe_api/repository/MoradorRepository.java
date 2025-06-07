package br.com.fiap.flood_safe_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.com.fiap.flood_safe_api.model.Comunidade;
import br.com.fiap.flood_safe_api.model.Morador;

@Repository
public interface MoradorRepository extends JpaRepository<Morador, Long>, JpaSpecificationExecutor<Morador> {

    /**
     * Encontra todos os moradores de uma comunidade específica, com paginação.
     * 
     * @param comunidadeId O ID da comunidade.
     * @param pageable     Configurações de paginação e ordenação.
     * @return Uma página de moradores.
     */
    Page<Morador> findByComunidadeIdComunidade(Long comunidadeId, Pageable pageable);

    /**
     * Encontra um morador pelo seu contato principal (que é único).
     * 
     * @param contatoPrincipal O contato principal do morador.
     * @return Um Optional contendo o morador se encontrado.
     */
    Optional<Morador> findByContatoPrincipal(String contatoPrincipal);

    /**
     * Encontra todos os moradores de uma comunidade.
     * 
     * @param comunidade A entidade Comunidade.
     * @return Uma lista de moradores.
     */
    List<Morador> findByComunidade(Comunidade comunidade);
}
