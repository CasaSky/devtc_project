@org.hibernate.annotations.TypeDef(name = "jsonb", typeClass = JsonbJavaType.class)
@org.hibernate.annotations.TypeDef(name = "enum", typeClass = EnumJavaType.class)

package com.casasky.core.hibernate;


import com.casasky.core.hibernate.enumeration.EnumJavaType;
import com.casasky.core.hibernate.jsonb.JsonbJavaType;