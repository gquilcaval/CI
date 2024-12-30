package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({StatusRefrigerio.NO_REFRIGERIO,
        StatusRefrigerio.SI_REFRIGERIO})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusRefrigerio {
    int NO_REFRIGERIO = 0;
    int SI_REFRIGERIO = 1;
}
