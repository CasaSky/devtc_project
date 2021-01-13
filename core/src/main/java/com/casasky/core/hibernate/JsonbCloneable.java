package com.casasky.core.hibernate;


/**
 * Interface to force clone implementation
 */
public interface JsonbCloneable extends Cloneable {

    Object clone();

}
