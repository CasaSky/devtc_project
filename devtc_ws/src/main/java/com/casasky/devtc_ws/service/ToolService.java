package com.casasky.devtc_ws.service;


import java.util.List;

import javax.persistence.EntityManager;

import com.casasky.core.service.TemplateBaseService;
import com.casasky.devtc_ws.entity.Maintenance;
import com.casasky.devtc_ws.entity.Tool;
import org.springframework.stereotype.Service;


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


}
