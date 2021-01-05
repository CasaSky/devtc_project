package com.casasky.core.entity;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@MappedSuperclass
@Getter
@SuperBuilder(toBuilder = true)
public class TemplateBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    protected TemplateBaseEntity() {
    }

}
