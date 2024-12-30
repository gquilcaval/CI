package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "int_IdentificadorTurno", indices = {@Index(value = "int_IdentificadorTurno",unique = true)})
public class ParcelaModel {

    @SerializedName("int_IdentificadorTurno")
    private int int_IdentificadorTurno;
    @SerializedName("vch_Descripcion")
    private String vch_Descripcion;
    @SerializedName("vch_HoraInicio")
    private String vch_HoraInicio;
    @SerializedName("vch_HoraFin")
    private String vch_HoraFin;
    @SerializedName("int_FlgEstadoTurno")
    private int int_FlgEstadoTurno;

    public int getInt_IdentificadorTurno() {
        return int_IdentificadorTurno;
    }

    public void setInt_IdentificadorTurno(int int_IdentificadorTurno) {
        this.int_IdentificadorTurno = int_IdentificadorTurno;
    }

    public String getVch_Descripcion() {
        return vch_Descripcion;
    }

    public void setVch_Descripcion(String vch_Descripcion) {
        this.vch_Descripcion = vch_Descripcion;
    }

    public String getVch_HoraInicio() {
        return vch_HoraInicio;
    }

    public void setVch_HoraInicio(String vch_HoraInicio) {
        this.vch_HoraInicio = vch_HoraInicio;
    }

    public String getVch_HoraFin() {
        return vch_HoraFin;
    }

    public void setVch_HoraFin(String vch_HoraFin) {
        this.vch_HoraFin = vch_HoraFin;
    }

    public int getInt_FlgEstadoTurno() {
        return int_FlgEstadoTurno;
    }

    public void setInt_FlgEstadoTurno(int int_FlgEstadoTurno) {
        this.int_FlgEstadoTurno = int_FlgEstadoTurno;
    }
}
