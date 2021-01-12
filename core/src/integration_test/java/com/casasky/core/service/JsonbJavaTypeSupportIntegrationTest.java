package com.casasky.core.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.casasky.core.entity.JsonbDummy;
import com.casasky.core.entity.TestDummy;

import com.casasky.core.hibernate.JsonbJavaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class JsonbJavaTypeSupportIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestDummyService testDummyService;

    @Test
    void test() {

        var jsonbDummy = new JsonbDummy("a");
        var testDummy = new TestDummy(jsonbDummy);
        testDummyService.persist(testDummy);

        List<TestDummy> all = testDummyService.findAll(TestDummy.class);
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactly(testDummy);

    }

}
