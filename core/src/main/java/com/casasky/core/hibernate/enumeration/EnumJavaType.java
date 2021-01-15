package com.casasky.core.hibernate.enumeration;


import java.util.Properties;

import com.casasky.core.hibernate.CustomSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;


public class EnumJavaType extends AbstractSingleColumnStandardBasicType<EnumDecorator> implements DynamicParameterizedType {

    private static final long serialVersionUID = -5121736839164413930L;


    public EnumJavaType() {
        super(CustomSqlTypeDescriptor.INSTANCE, new EnumJavaTypeDescriptor<>());
    }


    @Override
    public String getName() {
        return EnumJavaType.class.getSimpleName();
    }


    @Override
    public void setParameterValues(Properties parameters) {
        ((EnumJavaTypeDescriptor<EnumDecorator>) getJavaTypeDescriptor()).setParameterValues(parameters);
    }

}
