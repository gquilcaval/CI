package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({StatusEmpleado.EMPLEADO_LIBRE,
        StatusEmpleado.EMPLEADO_NO_LIBRE})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusEmpleado {
    int EMPLEADO_LIBRE = 1; //Al eliminar tareo modo: En Linea
    int EMPLEADO_NO_LIBRE = 2;
}
