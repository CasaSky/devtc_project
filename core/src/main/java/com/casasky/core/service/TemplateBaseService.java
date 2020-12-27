package com.casasky.core.service;


import static java.lang.String.format;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TemplateBaseService {

    @PersistenceContext
    private final EntityManager em;


    public TemplateBaseService(EntityManager em) {
        this.em = em;
    }


    public <T> void persist(T entity) {
        em.persist(entity);
    }


    public <T> List<T> findAll(Class<T> clazz) {

        return em.createQuery(format("select e from %s e", clazz.getSimpleName()), clazz).getResultList();

    }

}
