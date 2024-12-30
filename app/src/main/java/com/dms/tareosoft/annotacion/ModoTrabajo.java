package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({ModoTrabajo.LINEA,
        ModoTrabajo.BATCH})
@Retention(RetentionPolicy.SOURCE)
public @interface ModoTrabajo {
    int LINEA = 1;
    int BATCH = 2;
}
