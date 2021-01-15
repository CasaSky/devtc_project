package com.casasky.core.hibernate.jsonb;


import java.util.Properties;

import com.casasky.core.hibernate.CustomSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;


public class JsonbJavaType extends AbstractSingleColumnStandardBasicType<Object> implements DynamicParameterizedType {

    private static final long serialVersionUID = -151297218855388020L;


    public JsonbJavaType() {
        super(CustomSqlTypeDescriptor.INSTANCE, new JsonbJavaTypeDescriptor());
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