package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SelectTurno.PRIMERO,
        SelectTurno.SEGUNDO,
        SelectTurno.TERCERO,
        SelectTurno.CUARTO})
@Retention(RetentionPolicy.SOURCE)
public @interface SelectTurno {
    int PRIMERO = 1; //Al eliminar tareo modo: En Linea
    int SEGUNDO = 2;
    int TERCERO = 3;
    int CUARTO = 4;
}
