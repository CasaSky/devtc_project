package com.casasky.core.service;


import static java.lang.String.format;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import com.casasky.core.entity.Schema;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("integration-test")
public class BaseIntegrationTest {

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    protected JdbcTemplate jdbcTemplate;


    @BeforeEach
    public void truncate() {
        Schema.ALL_SCHEMA.forEach(schema -> jdbcTemplate.execute(format("truncate %s restart identity", String.join(",", allTables(schema)))));
    }


    protected <T> void persist(T entity) {
        var em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
    }


    private List<String> allTables(String schema) {
        return jdbcTemplate.queryForList("select format('%s.%s', schemaname, relname) from pg_stat_user_tables where schemaname = ?", String.class, schema);
    }

}
