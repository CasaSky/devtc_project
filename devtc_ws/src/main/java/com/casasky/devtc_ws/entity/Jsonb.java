package com.casasky.devtc_ws.entity;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;


//TODO refactore me
public class Jsonb implements UserType {
 
    @Override
    public int[] sqlTypes() {
        return new int[]{ Types.JAVA_OBJECT};
    }


    @Override
    public Class<Jsonb> returnedClass() {
        return Jsonb.class;
    }


    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        return false;
    }


    @Override
    public int hashCode(Object o) throws HibernateException {
        return 0;
    }


    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final SharedSessionContractImplementor session,
            final Object owner) throws HibernateException, SQLException {
        final String cellContent = rs.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cellContent.getBytes("UTF-8"), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }


    @Override
    public void nullSafeSet(final PreparedStatement ps, final Object value, final int idx,
            final SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            ps.setNull(idx, Types.OTHER);
            return;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final StringWriter w = new StringWriter();
            mapper.writeValue(w, value);
            w.flush();
            ps.setObject(idx, w.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }


    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        try {
            // use serialization to create a deep copy
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            Object obj = new ObjectInputStream(bais).readObject();
            bais.close();
            return obj;
        } catch (ClassNotFoundException | IOException ex) {
            throw new HibernateException(ex);
        }
    }


    @Override
    public boolean isMutable() {
        return false;
    }


    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return null;
    }


    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return null;
    }


    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return null;
    }

}