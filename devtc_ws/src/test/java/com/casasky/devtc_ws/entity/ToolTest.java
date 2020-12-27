package com.casasky.devtc_ws.entity;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class ToolTest {

    @Test
    void construct() {

        var name = "test";
        var tool = new Tool(name);
        assertThat(tool.getName()).isEqualTo(name);

    }

}