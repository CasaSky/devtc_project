package com.casasky.core.hibernate;

import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.util.*;

import com.casasky.core.util.TimeUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.annotations.common.reflection.XProperty;
import org.hibernate.annotations.common.reflection.java.JavaXMember;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.hibernate.usertype.DynamicParameterizedType;
import org.postgresql.util.PGobject;


class JsonbJavaTypeDescriptor extends AbstractTypeDescriptor<Object> implements DynamicParameterizedType {

    private static final long serialVersionUID = -4772021694138778956L;

    static final JsonbJavaTypeDescriptor INSTANCE = new JsonbJavaTypeDescriptor();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .setDateFormat(new StdDateFormat())
            .setTimeZone(TimeZone.getTimeZone(TimeUtil.DEFAULT_ZONE));

    private ObjectReader propertyReader;


    private JsonbJavaTypeDescriptor() {
        super(Object.class, JsonbMutabilityPlan.INSTANCE);
    }



    @Override
    public Object fromString(String string) {

        try {
            return propertyReader.readValue(string);
        }
        catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }

    }


    @Override
    public boolean areEqual(Object one, Object another) {

        return Objects.equals(one, another);

    }


    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(Object value, Class<X> type, WrapperOptions options) {

        if (value == null) {
            return null;
        }

        if (String.class.isAssignableFrom(type)) {

            try {
                return (X) OBJECT_MAPPER.writeValueAsString(value);
            } catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }

        }

        throw unknownUnwrap(type);

    }


    @Override
    public <X> Object wrap(X value, WrapperOptions options) {

        if (value == null) {
            return null;
        }

        if (value instanceof PGobject) {
            try {
                return propertyReader.readValue(((PGobject) value).getValue());
            }
            catch (JsonProcessingException e) {
                throw new UncheckedIOException(e);
            }
        }

        throw unknownUnwrap(value.getClass());

    }


    @Override
    public void setParameterValues(Properties parameters) {

        final var xProperty = (XProperty) parameters.get(DynamicParameterizedType.XPROPERTY);
        final var type = (xProperty instanceof JavaXMember) ?
                ((JavaXMember) xProperty).getJavaType() :
                ((DynamicParameterizedType.ParameterType) parameters.get(DynamicParameterizedType.PARAMETER_TYPE)).getReturnedClass();

        setPropertyReader(type);

    }


    private void setPropertyReader(Type type) {
        propertyReader = OBJECT_MAPPER.reader().forType(OBJECT_MAPPER.getTypeFactory().constructType(type));
    }


    private static class JsonbMutabilityPlan extends MutableMutabilityPlan<Object> {

        private static final long serialVersionUID = -3427312507708199793L;

        static final JsonbMutabilityPlan INSTANCE = new JsonbMutabilityPlan();

        @Override
        protected Object deepCopyNotNull(Object value) {

            // Immutable by default, no need to copy
            if (value.getClass().getName().equals("java.util.ImmutableCollections$AbstractImmutableCollection")) {
                return value;
            }

            return ObjectUtils.clone(value);

        }

    }

}
