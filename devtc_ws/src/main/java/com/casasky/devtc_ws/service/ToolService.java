package com.casasky.devtc_ws.service;


import javax.persistence.EntityManager;

import com.casasky.core.service.TemplateBaseService;
import org.springframework.stereotype.Service;


@Service
public class ToolService extends TemplateBaseService {

    public ToolService(EntityManager em) {
        super(em);
    }

}
