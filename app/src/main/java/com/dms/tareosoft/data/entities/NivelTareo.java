package com.dms.tareosoft.data.entities;

import androidx.room.Entity;
import androidx.room.Index;

import com.dms.tareosoft.util.TextUtil;
import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "idNivel",indices = {@Index(value = "idNivel",unique = true)})
public class NivelTareo {

    @SerializedName("int_IdentificadorNivel")
    private int idNivel;
    @SerializedName("vch_DescripcionNivel")
    private String descripcion;
    @SerializedName("int_OrdenNivel")
    private int orden;
    @SerializedName("int_IdentificadorClase")
    private int fkClase;

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public int getFkClase() {
        return fkClase;
    }

    public void setFkClase(int fkClase) {
        this.fkClase = fkClase;
    }

    @Override
    public String toString() {
        return "NivelTareo{" +
                "idNivel=" + idNivel +
                ", descripcion='" + descripcion + '\'' +
                ", orden=" + orden +
                ", fkClase=" + fkClase +
                '}';
    }
}
