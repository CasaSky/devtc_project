package com.casasky.devtc_ws.controller;

import com.casasky.devtc_ws.entity.Tool;


public class ToolDto {

    public String name;


    protected ToolDto() {
    }


    public ToolDto(String name) {
        this.name = name;
    }


    Tool entity() {
        return new Tool(name);
    }

}
