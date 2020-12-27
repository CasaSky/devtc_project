package com.casasky.devtc_ws.controller;

import com.casasky.devtc_ws.entity.Tool;


public class ToolDto {

    public String name;

    Tool entity() {
        return new Tool(name);
    }

}
