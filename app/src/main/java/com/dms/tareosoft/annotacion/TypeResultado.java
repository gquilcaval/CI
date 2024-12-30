package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TypeResultado.TIPO_RESULTADO_POR_EMPLEADO,
        TypeResultado.TIPO_RESULTADO_POR_TAREO})
@Retention(RetentionPolicy.SOURCE)
public @interface TypeResultado {
    int TIPO_RESULTADO_POR_EMPLEADO = 0; //Al eliminar tareo modo: En Linea
    int TIPO_RESULTADO_POR_TAREO = 1;
}
