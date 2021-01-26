package com.casasky.devtc_ws.service;

import javax.validation.constraints.NotEmpty;

import com.casasky.devtc_ws.entity.Tool;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class ToolDto {

    @NotEmpty
    public String name;


    public Tool entity() {
        return new Tool(name);
    }

}
