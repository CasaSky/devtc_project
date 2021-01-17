package com.casasky.devtc_ws.service;

import com.casasky.devtc_ws.entity.Tool;


public class ToolDto {

    public String name;
    public String lastReleaseVersion;
    public String downloadUrl;
    public String packageExtension;
    public String packageBinaryPath;


    protected ToolDto() {
    }


    public ToolDto(String name) {
        this.name = name;
    }


    public Tool entity() {
        return new Tool(name);
    }

}
