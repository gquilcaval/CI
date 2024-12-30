package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ResultadoPorEmpleadoModificado.NUEVO,
        ResultadoPorEmpleadoModificado.MODIFICADO})
@Retention(RetentionPolicy.SOURCE)
public @interface ResultadoPorEmpleadoModificado {
    int NUEVO = 1;
    int MODIFICADO = 2;
}
