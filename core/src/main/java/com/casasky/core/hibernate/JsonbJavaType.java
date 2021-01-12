package com.casasky.core.hibernate;


import java.util.Properties;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;


public class JsonbJavaType extends AbstractSingleColumnStandardBasicType<Object> implements DynamicParameterizedType {

    private static final long serialVersionUID = -151297218855388020L;


    public JsonbJavaType() {
        super(JsonbSqlTypeDescriptor.INSTANCE, JsonbJavaTypeDescriptor.INSTANCE);
    }


    @Override
    public String getName() {
        return JsonbJavaType.class.getSimpleName();
    }


    @Override
    public void setParameterValues(Properties parameters) {
        ((JsonbJavaTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }

}