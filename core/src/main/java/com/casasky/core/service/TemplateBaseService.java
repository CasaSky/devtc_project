package com.casasky.core.service;


import static java.lang.String.format;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;


@Transactional
public class TemplateBaseService<T> {

    @PersistenceContext
    protected final EntityManager em;


    public TemplateBaseService(EntityManager em) {
        this.em = em;
    }


    public void persist(T entity) {
        em.persist(entity);
    }


    public List<T> findAll(Class<T> clazz) {
        return em.createQuery(format("select e from %s e", clazz.getSimpleName()), clazz).getResultList();
    }


    protected T find(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }


    protected boolean doesExist(Class<T> clazz, Long id) {
        return find(clazz, id) != null;
    }

}
