package com.casasky.core.entity;


import java.util.Set;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TestDummy extends TemplateBaseEntity {

    @Type(type = "jsonb")
    private JsonbDummy jsonbAttribute;

    @Type(type = "jsonb")
    private Set<JsonbDummy> jsonbAttributes;

}
