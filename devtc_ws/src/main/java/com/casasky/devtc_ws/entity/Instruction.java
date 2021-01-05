package com.casasky.devtc_ws.entity;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
class Instruction {

    private final String description;
    private final String command;

}
