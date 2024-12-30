package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "INT_IDENTIFICADOR", indices = {@Index(value = "INT_IDENTIFICADOR",unique = true)})
public class ClaveDinamicaModel {

    @SerializedName("INT_IDENTIFICADOR")
    private int INT_IDENTIFICADOR;
    @SerializedName("FECHA_CREACION")
    private String FECHA_CREACION;
    @SerializedName("CONTRASENA")
    private String CONTRASENA;
    @SerializedName("FECHA_MODIFICACION")
    private String FECHA_MODIFICACION;

    public int getINT_IDENTIFICADOR() {
        return INT_IDENTIFICADOR;
    }

    public void setINT_IDENTIFICADOR(int INT_IDENTIFICADOR) {
        this.INT_IDENTIFICADOR = INT_IDENTIFICADOR;
    }

    public String getFECHA_CREACION() {
        return FECHA_CREACION;
    }

    public void setFECHA_CREACION(String FECHA_CREACION) {
        this.FECHA_CREACION = FECHA_CREACION;
    }

    public String getCONTRASENA() {
        return CONTRASENA;
    }

    public void setCONTRASENA(String CONTRASENA) {
        this.CONTRASENA = CONTRASENA;
    }

    public String getFECHA_MODIFICACION() {
        return FECHA_MODIFICACION;
    }

    public void setFECHA_MODIFICACION(String FECHA_MODIFICACION) {
        this.FECHA_MODIFICACION = FECHA_MODIFICACION;
    }
}
