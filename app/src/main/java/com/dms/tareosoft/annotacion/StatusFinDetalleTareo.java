package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({StatusFinDetalleTareo.TAREO_DETALLE_NO_FINALIZADO,
        StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusFinDetalleTareo {
    int TAREO_DETALLE_NO_FINALIZADO = 0; //Al eliminar tareo modo: En Linea
    int TAREO_DETALLE_FINALIZADO = 1;
}
