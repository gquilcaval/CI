package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({StatusTareo.TAREO_ELIMINADO,
        StatusTareo.TAREO_ACTIVO,
        StatusTareo.TAREO_FINALIZADO,
        StatusTareo.TAREO_LIQUIDADO})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusTareo {
    int TAREO_ELIMINADO = 0; //Al eliminar tareo modo: En Linea
    int TAREO_ACTIVO = 1;
    int TAREO_FINALIZADO = 2;
    int TAREO_LIQUIDADO = 3;
}
