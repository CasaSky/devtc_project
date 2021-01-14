package com.casasky.devtc_ws.service;


import static org.assertj.core.api.Assertions.assertThat;

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
        var tools = toolService.findAll(Tool.class);
        assertThat(tools).usingRecursiveFieldByFieldElementComparator().containsExactly(tool);

    }

}