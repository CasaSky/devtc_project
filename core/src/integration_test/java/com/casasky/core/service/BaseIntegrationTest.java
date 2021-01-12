package com.casasky.core.service;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("integration-test")
public class BaseIntegrationTest {

    @Autowired
    private EntityManagerFactory emf;

    @PersistenceContext
    private EntityManager em;


    @BeforeEach
    public void truncate() {

        var em = emf.createEntityManager();
        em.getTransaction().begin();
        // todo way to scan all tables
        em.createNativeQuery("truncate table tool_schema.tool").executeUpdate();
        em.createNativeQuery("truncate table test_dummy").executeUpdate();
        em.getTransaction().commit();

    }


    public <T> void persist(T entity) {
        em.persist(entity);
    }

}
