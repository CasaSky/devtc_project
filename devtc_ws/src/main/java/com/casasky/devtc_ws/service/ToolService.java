package com.casasky.devtc_ws.service;


import java.util.List;

import javax.persistence.EntityManager;

import com.casasky.core.service.TemplateBaseService;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.Tool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ToolService extends TemplateBaseService<Tool> {

    public ToolService(EntityManager em) {
        super(em);
    }


    private void save(Tool entity) {
        super.persist(entity);
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
                .getSingleResult();
    }

    String findNameById(Long id) {
        return em.createQuery("select t.name from Tool t where t.id = :id", String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

}
