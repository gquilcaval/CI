package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({SyncExitosa.NO_EXITOSA,
        SyncExitosa.EXITOSA})
@Retention(RetentionPolicy.SOURCE)
public @interface SyncExitosa {
    int NO_EXITOSA = 0;
    int EXITOSA = 1;
}
