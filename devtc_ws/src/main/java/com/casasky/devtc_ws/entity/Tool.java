package com.casasky.devtc_ws.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.casasky.core.entity.TemplateBaseEntity;


@Entity
@Table(schema = "tools_schema")
public class Tool extends TemplateBaseEntity {

    private String name;

    protected Tool() {
    }

    public Tool(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

}
