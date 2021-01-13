package com.casasky.core.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import com.casasky.core.entity.JsonbDummy;
import com.casasky.core.entity.TestDummy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class JsonbJavaTypeSupportIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private TestDummyService testDummyService;

    @Test
    void test() {

        var jsonbAttribute1 = new JsonbDummy("a");
        var jsonbAttribute2 = new JsonbDummy("b");
        var testDummy = new TestDummy(jsonbAttribute1, Set.of(jsonbAttribute1, jsonbAttribute2));
        testDummyService.persist(testDummy);

        List<TestDummy> all = testDummyService.findAll(TestDummy.class);
        assertThat(all).usingRecursiveFieldByFieldElementComparator().containsExactly(testDummy);

    }

}
