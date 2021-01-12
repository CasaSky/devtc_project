package com.casasky.core.service;


import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;


@Service
class TestDummyService extends TemplateBaseService {

    public TestDummyService(EntityManager em) {
            super(em);
        }

}