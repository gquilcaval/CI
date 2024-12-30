package com.dms.tareosoft.annotacion;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({TypeView.HEADER,
        TypeView.SUB_HEADER,
        TypeView.BODY})
@Retention(RetentionPolicy.SOURCE)
public @interface TypeView {
    int HEADER = 1;
    int SUB_HEADER = 2;
    int BODY = 3;
}
