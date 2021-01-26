package com.casasky.devtc_ws.service;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.casasky.core.service.TemplateBaseService;
import com.casasky.devtc_ws.entity.Tool;
import com.casasky.devtc_ws.service.exception.DuplicateToolException;
import org.springframework.stereotype.Service;


@Service
public class ToolService extends TemplateBaseService<Tool> {

    public ToolService(EntityManager em) {
        super(em);
    }


    public void create(ToolDto tool) {
        save(tool.entity());
    }

    private void save(Tool entity) {
        try {
            super.persist(entity);
        } catch (PersistenceException e) {
            throw new DuplicateToolException(entity.getName());
        }
    }


    List<Tool> findAll() {
        return super.findAll(Tool.class);
    }


    Tool find(Long id) {
        return find(Tool.class, id);
    }

    boolean doesExist(Long id) {
        return doesExist(Tool.class, id);
    }

    boolean doesExist(String name) {
        return findIdByName(name) != null;
    }


    Long findIdByName(String name) {
        return em.createQuery("select t.id from Tool t where t.name = :name", Long.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

    String findNameById(Long id) {
        return em.createQuery("select t.name from Tool t where t.id = :id", String.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
