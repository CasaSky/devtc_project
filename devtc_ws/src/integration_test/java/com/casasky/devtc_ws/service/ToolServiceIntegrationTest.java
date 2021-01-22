package com.casasky.devtc_ws.service;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.casasky.core.service.BaseIntegrationTest;
import com.casasky.devtc_ws.entity.Tool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ToolServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ToolService toolService;


    @Test
    void persistence() {

        var tool = new Tool("test");
        toolService.persist(tool);

        Tool toolFromDB = toolService.find(tool.getId());
        assertThat(toolFromDB).usingRecursiveComparison().isEqualTo(tool);

    }


    @Test
    void findAll() {

        var tool = new Tool("java");
        toolService.persist(tool);

        List<Tool> tools = toolService.findAll();
        assertThat(tools).usingRecursiveFieldByFieldElementComparator().containsExactly(tool);

    }


    @Test
    void doesExist() {

        assertThat(toolService.doesExist(1L)).isFalse();

        var tool = new Tool("test");
        toolService.persist(tool);

        assertThat(toolService.doesExist(tool.getId())).isTrue();

    }


    @Test
    void doesExistName() {
        var java = "java";
        assertThat(toolService.doesExist(java)).isFalse();

        var tool = new Tool(java);
        toolService.persist(tool);

        assertThat(toolService.doesExist(java)).isTrue();
    }

    @Test
    void findIdByName() {
        var java = "java";
        var javaTool = new Tool(java);
        toolService.persist(javaTool);

        assertThat(toolService.findIdByName(java)).isEqualTo(javaTool.getId());
    }


    @Test
    void findNameById() {
        var java = "java";
        var javaTool = new Tool(java);
        toolService.persist(javaTool);

        assertThat(toolService.findNameById(javaTool.getId())).isEqualTo(java);
    }


}