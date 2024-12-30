package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TypeUser.ADMIN,
        TypeUser.COMUN})
@Retention(RetentionPolicy.SOURCE)
public @interface TypeUser {
    int ADMIN = 1;
    int COMUN = 2;
}
