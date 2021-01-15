package com.casasky.core.entity;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;


@Entity
@Table(schema = "core")
@NoArgsConstructor
@AllArgsConstructor
public class TestDummy extends TemplateBaseEntity {

    @Type(type = "jsonb")
    private JsonbDummy jsonbAttribute;

    @Type(type = "jsonb")
    private Set<JsonbDummy> jsonbAttributes;

}
