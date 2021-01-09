package com.casasky.devtc_ws.entity;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
// TODO must be immutable
class Instruction {

    private String description;
    private String command;


    protected Instruction() {
    }


    public Instruction(String description, String command) {
        this.description = description;
        this.command = command;
    }

}
