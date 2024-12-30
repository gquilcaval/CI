package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({StatusIniDetalleTareo.TAREO_DETALLE_NO_INICIADO,
        StatusIniDetalleTareo.TAREO_DETALLE_INICIADO})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusIniDetalleTareo {
    int TAREO_DETALLE_NO_INICIADO = 0; //Al eliminar tareo modo: En Linea
    int TAREO_DETALLE_INICIADO = 1;
}
