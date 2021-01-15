package com.casasky.core.hibernate.enumeration;


import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.ClassUtils;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.annotations.common.reflection.java.JavaXMember;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.postgresql.util.PGobject;


/**
 * notice: This dynamic type is not allowed to provide a singleton access, otherwise it will use the same reader for different jsonb types
 */
class EnumJavaTypeDescriptor<Y extends Enum<Y>> extends AbstractTypeDescriptor<Y> implements DynamicParameterizedType {

    private static final long serialVersionUID = 4297680040743057234L;

    private Class<Y> genericTypeClass;


    @SuppressWarnings("unchecked")
    EnumJavaTypeDescriptor() {
        // EnumDecorator is not used for collecting data, it is there to only satisfy the super call of AbstractTypeDescriptor
        super((Class<Y>) EnumDecorator.class);
    }

    @Override
    public Y fromString(String string) {
        return string == null ? null : Enum.valueOf(genericTypeClass, string);
    }


    @Override
    public boolean areEqual(Y one, Y another) {
        return Objects.equals(one, another);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(Y value, Class<X> type, WrapperOptions options) {
        if ( value == null ) {
            return null;
        }

        return (X) value.name();
    }


    @Override
    public <X> Y wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (value instanceof PGobject) {
            return Enum.valueOf(genericTypeClass, Objects.requireNonNull(((PGobject) value).getValue()).trim());
        }

        throw unknownWrap(value.getClass());
    }


    @Override
    public void setParameterValues(Properties parameters) {
        genericTypeClass = provideGenericTypeClass(parameters);
    }


    @SuppressWarnings("unchecked")
    private Class<Y> provideGenericTypeClass(Properties parameters) {
        final var xProperty = (XProperty) parameters.get(DynamicParameterizedType.XPROPERTY);
        final var type = (xProperty instanceof JavaXMember) ?
                ((JavaXMember) xProperty).getJavaType():
                ((DynamicParameterizedType.ParameterType) parameters.get(DynamicParameterizedType.PARAMETER_TYPE)).getReturnedClass();

        try {
            return (Class<Y>) ClassUtils.getClass(type.getTypeName());
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
