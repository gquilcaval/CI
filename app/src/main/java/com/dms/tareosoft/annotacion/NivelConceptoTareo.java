package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({NivelConceptoTareo.CONCEPTO_1,
        NivelConceptoTareo.CONCEPTO_2,
        NivelConceptoTareo.CONCEPTO_3,
        NivelConceptoTareo.CONCEPTO_4,
        NivelConceptoTareo.CONCEPTO_5})
@Retention(RetentionPolicy.SOURCE)
public @interface NivelConceptoTareo {
    int CONCEPTO_1 = 1; //Al eliminar tareo modo: En Linea
    int CONCEPTO_2 = 2;
    int CONCEPTO_3 = 3;
    int CONCEPTO_4 = 4;
    int CONCEPTO_5 = 5;
}
