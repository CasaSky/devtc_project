package com.casasky.core.entity;


import com.casasky.core.hibernate.jsonb.JsonbCloneable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Getter
public class JsonbDummy implements JsonbCloneable {

    private String x;


    @Override
    public Object clone() {

        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new JsonbDummy(this.x);
        }

    }

}