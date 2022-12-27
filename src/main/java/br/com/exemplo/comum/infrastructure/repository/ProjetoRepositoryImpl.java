package br.com.exemplo.comum.infrastructure.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroProjeto;
import br.com.exemplo.comum.domain.model.entities.Projeto;
import br.com.exemplo.comum.domain.model.entities.Projeto_;
import br.com.exemplo.comum.domain.repository.ProjetoRepositoryCustom;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjetoRepositoryImpl implements ProjetoRepositoryCustom {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Long contaPorFiltros(FiltroProjeto filtros) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        
        Root<Projeto> root = criteria.from(Projeto.class);
        criteria.select(builder.count(root));
        
        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        criteria.where(predicates.toArray(new Predicate[0]));
        return manager.createQuery(criteria).getSingleResult();
    }

    @Override
    public List<Projeto> pesquisaPorFiltros(FiltroProjeto filtros, Pageable paginacao) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Projeto> criteria = builder.createQuery(Projeto.class);
        Root<Projeto> root = criteria.from(Projeto.class);

        criteria.orderBy(builder.asc(root.get(Projeto_.NOME)));

        List<Predicate> predicates = aplicaFiltros(filtros, root, builder);
        criteria.where(predicates.toArray(new Predicate[0]));

        return manager.createQuery(criteria)
                .setFirstResult((paginacao.getPageNumber() - 1) * paginacao.getPageSize())
                .setMaxResults(paginacao.getPageSize())
                .getResultList();
    }

    private List<Predicate> aplicaFiltros(FiltroProjeto filtros, Root<Projeto> root, CriteriaBuilder builder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.isFalse(root.get(Projeto_.REMOVIDO)));

        if(!StringUtils.isEmpty(filtros.nome())) {
            predicates.add(
                    builder.like(root.get(Projeto_.NOME), Utilitarios.likeFunction(filtros.nome())));
        }
        return predicates;
    }
}