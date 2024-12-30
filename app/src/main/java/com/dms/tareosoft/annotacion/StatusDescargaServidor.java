package com.dms.tareosoft.annotacion;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({StatusDescargaServidor.PENDIENTE,
        StatusDescargaServidor.ENVIADO,
        StatusDescargaServidor.BACKUP})
@Retention(RetentionPolicy.SOURCE)
public @interface StatusDescargaServidor {
    String PENDIENTE = "P"; // Parcialmente enviado al servidor (Las tablas relacionadas estan pendientes de envio)
    String ENVIADO = "E"; // Se envio correctamente al servidor y se reserva en la BD por x días
    String BACKUP = "B";
}
