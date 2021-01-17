package com.casasky.devtc_ws.entity;


import com.casasky.core.hibernate.jsonb.JsonbCloneable;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class Instruction implements JsonbCloneable {

    private String description;
    private String command;


    protected Instruction() {
    }


    public Instruction(String description, String command) {
        this.description = description;
        this.command = command;
    }


    @Override
    public Object clone() {

        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new Instruction(this.description, this.command);
        }

    }

}
