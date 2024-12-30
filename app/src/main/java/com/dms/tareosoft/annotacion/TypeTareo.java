package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TypeTareo.TIPO_TAREO_JORNAL,
        TypeTareo.TIPO_TAREO_DESTAJO,
        TypeTareo.TIPO_TAREO_TAREA})
@Retention(RetentionPolicy.SOURCE)
public @interface TypeTareo {
    int TIPO_TAREO_JORNAL = 0;
    int TIPO_TAREO_DESTAJO = 1;
    int TIPO_TAREO_TAREA = 2;
}
